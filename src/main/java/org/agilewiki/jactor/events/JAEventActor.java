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
package org.agilewiki.jactor.events;

import org.agilewiki.jactor.concurrent.ThreadManager;

/**
 * An actor which passes one-way message (events).
 * 
 * @param <E> The type of event.
 */
abstract public class JAEventActor<E> 
        implements ActiveEventProcessor<E>, EventDestination<E> {

    /**
     * The actor's mailbox.
     */
    private EventQueue<E> eventQueue;

    /**
     * Create a JAEventActor
     * 
     * @param threadManager Provides a thread for processing dispatched events.
     */
    public JAEventActor(ThreadManager threadManager) {
        eventQueue = new JAEventQueue<E>(threadManager);
        eventQueue.setEventProcessor(this);
    }

    /**
     * Send an event.
     *
     * @param destination Where the event is to be sent.
     * @param event The event to be sent.
     */
    public void send(EventDestination<E> destination, E event) {
        destination.putEvent(event);
    }

    /**
     * The putEvent method adds an events to the queue of events to be processed.
     *
     * @param event The events to be processed.
     */
    @Override
    public void putEvent(E event) {
        eventQueue.putEvent(event);
    }

    /**
     * The haveEvents method is called when
     * there may be one or more pending events.
     */
    @Override
    public void haveEvents() {
        eventQueue.dispatchEvents();
    }
}
