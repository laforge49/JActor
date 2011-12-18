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

public class JAPCFuture {
    /**
     * Used to wake up the sending thread when a response is received.
     */
    private Semaphore done;

    /**
     * The response received.
     */
    private transient Object result;

    private BufferedEventsDestination<APCMessage> bufferedEventsDestination =
            new BufferedEventsDestination<APCMessage>() {
                @Override
                public void putBufferedEvents(ArrayList<APCMessage> bufferedEvents) {
                    APCResponse apcResponse = (APCResponse) bufferedEvents.get(0);
                    result = apcResponse.getResult();
                    done.release();
                }
            };

    private RequestSource requestSource = new RequestSource() {
        @Override
        public void responseFrom(BufferedEventsQueue<APCMessage> eventQueue, APCResponse apcResponse) {
            eventQueue.send(bufferedEventsDestination, apcResponse);
        }

        @Override
        public void send(BufferedEventsDestination<APCMessage> destination, JAPCRequest japcRequest) {
            ArrayList<APCMessage> bufferedEvents = new ArrayList<APCMessage>(1);
            bufferedEvents.add(japcRequest);
            destination.putBufferedEvents(bufferedEvents);
        }
    };

    public Object send(APCActor actor, Object data) throws Exception {
        done = new Semaphore(0);
        actor.acceptRequest(requestSource, data, null);
        done.acquire();
        done = null;
        if (result instanceof Exception) throw (Exception) result;
        return result;
    }
}
