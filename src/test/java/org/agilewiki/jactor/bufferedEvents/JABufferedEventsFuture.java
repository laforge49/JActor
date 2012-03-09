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
package org.agilewiki.jactor.bufferedEvents;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;

/**
 * Test code: JABufferedEventsFuture is used to send
 * events to a BufferedEventsDestination, like JABufferedEventsActor, and then wait
 * for a return event.
 *
 * @param <E> The type of event.
 */
final public class JABufferedEventsFuture<E> implements BufferedEventsDestination<E> {
    /**
     * Used to wake up the sending thread when a message is received.
     */
    private Semaphore done;

    /**
     * The message received.
     */
    private transient E result;

    /**
     * Send an event and then wait for the response, which is returned.
     *
     * @param actor The actor that is to receive the event.
     * @param event The event to be sent.
     */
    public E send(BufferedEventsActor<E> actor, E event) {
        done = new Semaphore(0);
        ArrayList<E> bufferedEvents = new ArrayList<E>(1);
        bufferedEvents.add(event);
        actor.putBufferedEvents(bufferedEvents);
        try {
            done.acquire();
        } catch (InterruptedException e) {
        }
        done = null;
        return result;
    }

    /**
     * The putBufferedEvents method adds events to be processed.
     *
     * @param bufferedEvents The events to be processed.
     */
    @Override
    public void putBufferedEvents(ArrayList<E> bufferedEvents) {
        result = bufferedEvents.get(0);
        done.release();
    }
}
