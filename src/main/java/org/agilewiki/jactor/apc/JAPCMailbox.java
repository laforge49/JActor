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

import org.agilewiki.jactor.bufferedEvents.BufferedEventsDestination;
import org.agilewiki.jactor.bufferedEvents.BufferedEventsQueue;
import org.agilewiki.jactor.bufferedEvents.JABufferedEventsQueue;
import org.agilewiki.jactor.concurrent.ThreadManager;
import org.agilewiki.jactor.events.ActiveEventProcessor;

import java.util.ArrayList;

final public class JAPCMailbox implements APCMailbox {

    private APCRequest currentRequest;

    private BufferedEventsQueue<APCMessage> eventQueue;

    /**
     * The requestProcessor is used to process requests.
     */
    ActiveEventProcessor<APCRequest> requestProcessor;

    public JAPCMailbox(BufferedEventsQueue<APCMessage> eventQueue) {
        this.eventQueue = eventQueue;
        eventQueue.setEventProcessor(new ActiveEventProcessor<APCMessage>() {
            @Override
            public void haveEvents() {
                dispatchEvents();
            }

            @Override
            public void processEvent(APCMessage event) {
                if (event instanceof APCRequest) {
                    currentRequest = (APCRequest) event;
                    try {
                        requestProcessor.processEvent(currentRequest);
                    } catch (Exception ex) {
                        processException(ex);
                    }
                } else {
                    APCResponse apcResponse = (APCResponse) event;
                    currentRequest = apcResponse.getOldAPCRequest();
                    Object data = apcResponse.getResult();
                    if (data != null && data instanceof Exception)
                        processException((Exception) data);
                    else try {
                        apcResponse.getResponseDestination().process(apcResponse.getResult());
                    } catch (Exception ex) {
                        processException(ex);
                    }
                }
            }

            private void processException(Exception ex) {
                ExceptionHandler exceptionHandler = getExceptionHandler();
                if (exceptionHandler == null) response(ex);
                else try {
                    exceptionHandler.process(ex);
                } catch (Exception ex2) {
                    response(ex2);
                }
            }
        });
    }

    public JAPCMailbox(ThreadManager threadManager) {
        this(new JABufferedEventsQueue<APCMessage>(threadManager));
    }

    public APCRequest getCurrentRequest() {
        return currentRequest;
    }

    public void setCurrentRequest(APCRequest currentRequest) {
        this.currentRequest = currentRequest;
    }

    public ExceptionHandler getExceptionHandler() {
        return currentRequest.getExceptionHandler();
    }

    public void setExceptionHandler(ExceptionHandler exceptionHandler) {
        currentRequest.setExceptionHandler(exceptionHandler);
    }

    /**
     * Specifies the object which will process the dispatched events.
     *
     * @param requestProcessor Processes the dispatched events.
     */
    @Override
    public void setEventProcessor(ActiveEventProcessor<APCRequest> requestProcessor) {
        this.requestProcessor = requestProcessor;
    }

    /**
     * The isEmpty method returns true when there are no pending events,
     * though the results may not always be correct due to concurrency issues.
     */
    @Override
    public boolean isEmpty() {
        return eventQueue.isEmpty();
    }

    /**
     * The dispatchEvents method processes any events in the queue.
     * True is returned if any events were actually processed.
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
    public void putBufferedEvents(ArrayList<APCMessage> bufferedEvents) {
        eventQueue.putBufferedEvents(bufferedEvents);
    }

    /**
     * Send any pending events.
     */
    @Override
    public void sendPendingEvents() {
        eventQueue.sendPendingEvents();
    }

    /**
     * Set the initial capacity for buffered outgoing events.
     *
     * @param initialBufferCapacity The initial capacity for buffered outgoing events.
     */
    @Override
    public void setInitialBufferCapacity(int initialBufferCapacity) {
        eventQueue.setInitialBufferCapacity(initialBufferCapacity);
    }

    /**
     * Buffer the event for subsequent sending.
     *
     * @param destination Buffered events receiver.
     * @param request     The request to be sent.
     */
    @Override
    public void send(BufferedEventsDestination<APCMessage> destination, APCRequest request) {
        request.setOldRequest(currentRequest);
        eventQueue.send(destination, request);
    }

    @Override
    public void response(Object data) {
        currentRequest.response(eventQueue, data);
    }
}
