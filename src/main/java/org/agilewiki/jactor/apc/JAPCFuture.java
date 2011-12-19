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

import java.util.ArrayList;
import java.util.concurrent.Semaphore;

/**
 * Used mostly in testing to send a request to an actor and wait for a response.
 */
public class JAPCFuture {
    /**
     * Used to wake up the sending thread when a response is received.
     */
    private Semaphore done;

    /**
     * The response received.
     */
    private transient Object result;

    /**
     * Receives the response as a bufferedEvent.
     */
    private BufferedEventsDestination<JAPCMessage> bufferedEventsDestination =
            new BufferedEventsDestination<JAPCMessage>() {
                @Override
                public void putBufferedEvents(ArrayList<JAPCMessage> bufferedEvents) {
                    JAPCResponse japcResponse = (JAPCResponse) bufferedEvents.get(0);
                    result = japcResponse.getUnwrappedResponse();
                    done.release();
                }
            };

    /**
     * Serves as the originator of a request.
     */
    private APCRequestSource requestSource = new APCRequestSource() {
        @Override
        public void responseFrom(BufferedEventsQueue<JAPCMessage> eventQueue, JAPCResponse japcResponse) {
            eventQueue.send(bufferedEventsDestination, japcResponse);
        }

        @Override
        public void send(BufferedEventsDestination<JAPCMessage> destination, JAPCRequest japcRequest) {
            ArrayList<JAPCMessage> bufferedEvents = new ArrayList<JAPCMessage>(1);
            bufferedEvents.add(japcRequest);
            destination.putBufferedEvents(bufferedEvents);
        }
    };

    /**
     * Sends a request and waits for a response.
     *
     * @param actor            The target actor.
     * @param unwrappedRequest The unwrapped request.
     * @return The unwrapped response.
     * @throws Exception Any uncaught exceptions raised while processing the request.
     */
    public Object send(APCActor actor, Object unwrappedRequest) throws Exception {
        done = new Semaphore(0);
        actor.acceptRequest(requestSource, unwrappedRequest, null);
        done.acquire();
        done = null;
        if (result instanceof Exception) throw (Exception) result;
        return result;
    }
}
