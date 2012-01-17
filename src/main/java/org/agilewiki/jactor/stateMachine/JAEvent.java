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
package org.agilewiki.jactor.stateMachine;

import org.agilewiki.jactor.Actor;
import org.agilewiki.jactor.ExceptionHandler;
import org.agilewiki.jactor.Mailbox;
import org.agilewiki.jactor.ResponseProcessor;
import org.agilewiki.jactor.apc.JAMessage;
import org.agilewiki.jactor.apc.JARequest;
import org.agilewiki.jactor.apc.JAResponse;
import org.agilewiki.jactor.bufferedEvents.BufferedEventsDestination;
import org.agilewiki.jactor.bufferedEvents.BufferedEventsQueue;
import org.agilewiki.jactor.lpc.RequestSource;

import java.util.ArrayList;

/**
 * <p>
 * Used to send a request to an actor without having to wait for a response.
 * Any responses or exceptions from processing the request are discarded.
 * </p>
 */
public class JAEvent {

    /**
     * Receives the response as a bufferedEvent and discards it.
     */
    private BufferedEventsDestination<JAMessage> bufferedEventsDestination =
            new BufferedEventsDestination<JAMessage>() {
                @Override
                public void putBufferedEvents(ArrayList<JAMessage> bufferedEvents) {}
            };

    /**
     * Serves as the originator of a request.
     */
    private RequestSource requestSource = new RequestSource() {
        @Override
        final public Mailbox getMailbox() {
            return null;
        }

        @Override
        public ExceptionHandler getExceptionHandler() {
            return null;
        }

        @Override
        public void setExceptionHandler(ExceptionHandler exceptionHandler) {
            throw new UnsupportedOperationException();
        }

        @Override
        final public void responseFrom(final BufferedEventsQueue<JAMessage> eventQueue,
                                       final JAResponse japcResponse) {
            eventQueue.send(bufferedEventsDestination, japcResponse);
        }

        @Override
        final public void send(final BufferedEventsDestination<JAMessage> destination,
                               final JARequest japcRequest) {
            final ArrayList<JAMessage> bufferedEvents = new ArrayList<JAMessage>(1);
            bufferedEvents.add(japcRequest);
            destination.putBufferedEvents(bufferedEvents);
        }
    };

    /**
     * Discards a synchronous response.
     */
    private ResponseProcessor rp = new ResponseProcessor() {
        @Override
        public void process(Object response) throws Exception {}
    };

    /**
     * Sends a request without having to wait for a response.
     *
     * @param actor   The target actor.
     * @param request The request.
     */
    public void send(final Actor actor,
                       final Object request)
            throws Exception {
        actor.acceptRequest(requestSource, request, rp);
    }
}
