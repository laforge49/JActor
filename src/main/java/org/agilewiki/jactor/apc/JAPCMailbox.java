/*
 * Copyright 2011 Bill La Forge
 *
 * This file is part of AgileWiki and is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License (LGPL) as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This code is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA
 * or navigate to the following url http://www.gnu.org/licenses/lgpl-2.1.txt
 *
 * Note however that only Scala, Java and JavaScript files are being covered by LGPL.
 * All other files are covered by the Common Public License (CPL).
 * A copy of this license is also included and can be
 * found as well at http://www.opensource.org/licenses/cpl1.0.txt
 */
package org.agilewiki.jactor.apc;

import org.agilewiki.jactor.ExceptionHandler;
import org.agilewiki.jactor.bufferedEvents.BufferedEventsDestination;
import org.agilewiki.jactor.bufferedEvents.BufferedEventsQueue;
import org.agilewiki.jactor.bufferedEvents.JABufferedEventsQueue;
import org.agilewiki.jactor.concurrent.ThreadManager;
import org.agilewiki.jactor.events.ActiveEventProcessor;

import java.util.ArrayList;

/**
 * An implementation of APCMailbox.
 */
public class JAPCMailbox implements APCMailbox {

    /**
     * The request message currently being processed.
     */
    private JARequest currentRequest;

    /**
     * The lower-level mailbox which transports messages as 1-way events.
     */
    private BufferedEventsQueue<JAMessage> eventQueue;

    /**
     * Create a JAPCMailbox.
     * Use this constructor when providing an implementation of BufferedEventsQueue
     * other than JABufferedEventsQueue.
     *
     * @param eventQueue The lower-level mailbox which transports messages as 1-way events.
     */
    public JAPCMailbox(BufferedEventsQueue<JAMessage> eventQueue) {
        this.eventQueue = eventQueue;
        eventQueue.setEventProcessor(new ActiveEventProcessor<JAMessage>() {
            @Override
            public void haveEvents() {
                dispatchEvents();
            }

            @Override
            public void processEvent(JAMessage event) {
                if (event instanceof JARequest) {
                    currentRequest = (JARequest) event;
                    try {
                        currentRequest.getRequestProcessor().processRequest(currentRequest);
                    } catch (Exception ex) {
                        ExceptionHandler exceptionHandler = currentRequest.getExceptionHandler();
                        if (exceptionHandler == null) response(ex);
                        else try {
                            exceptionHandler.process(ex);
                        } catch (Exception ex2) {
                            response(ex2);
                        }
                    }
                } else {
                    JAResponse jaResponse = (JAResponse) event;
                    try {
                        jaResponse.getResponseProcessor().process(jaResponse.getUnwrappedResponse());
                    } catch (Exception e) {
                        e.printStackTrace();
                        throw new UnsupportedOperationException(e);
                    }
                }
            }
        });
    }

    /**
     * Create a JAPCMailbox.
     *
     * @param threadManager Provides a thread for processing dispatched events.
     */
    public JAPCMailbox(ThreadManager threadManager) {
        this(new JABufferedEventsQueue<JAMessage>(threadManager));
    }

    /**
     * Returns the request message being processed.
     *
     * @return The request message being processed.
     */
    final public JARequest getCurrentRequest() {
        return currentRequest;
    }

    /**
     * Assigns the request message to be processed.
     *
     * @param currentRequest The request message being processed.
     */
    final public void setCurrentRequest(JARequest currentRequest) {
        this.currentRequest = currentRequest;
    }

    /**
     * The isEmpty method returns true when there are no pending messages,
     * though the results may not always be correct due to concurrency issues.
     */
    @Override
    final public boolean isEmpty() {
        return eventQueue.isEmpty();
    }

    /**
     * The dispatchMessages method processes any messages in the queue.
     * True is returned if any messages were actually processed.
     */
    @Override
    public boolean dispatchEvents() {
        return eventQueue.dispatchEvents();
    }

    /**
     * The putBufferedEvents method adds events to be processed.
     *
     * @param bufferedEvents The events to be processed.
     */
    @Override
    final public void putBufferedEvents(ArrayList<JAMessage> bufferedEvents) {
        eventQueue.putBufferedEvents(bufferedEvents);
    }

    /**
     * Send any pending Messages.
     */
    @Override
    final public void sendPendingMessages() {
        eventQueue.sendPendingEvents();
    }

    /**
     * Set the initial capacity for buffered outgoing messages.
     *
     * @param initialBufferCapacity The initial capacity for buffered outgoing messages.
     */
    @Override
    final public void setInitialBufferCapacity(int initialBufferCapacity) {
        eventQueue.setInitialBufferCapacity(initialBufferCapacity);
    }

    /**
     * Buffer the request for subsequent sending.
     *
     * @param destination Buffered events receiver.
     * @param request     The request to be sent.
     */
    @Override
    public void send(BufferedEventsDestination<JAMessage> destination, JARequest request) {
        eventQueue.send(destination, request);
    }

    /**
     * _ReturnF the response for processing.
     *
     * @param unwrappedResponse
     */
    @Override
    final public void response(Object unwrappedResponse) {
        if (currentRequest.isActive()) {
            currentRequest.inactive();
            currentRequest.response(eventQueue, unwrappedResponse);
        }
    }
}
