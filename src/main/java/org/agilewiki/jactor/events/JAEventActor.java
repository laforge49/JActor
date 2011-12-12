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
 * Using a simple echo test, JAEventActor takes 123 nanoseconds to send a message
 * when running with a single thread and 252 nanoseconds when running with multiple threads.
 * (Tests were done on an Intel Core i5 CPU M 540 @ 2.53GHz.)
 *
 * @param <E> The type of event.
 */
abstract public class JAEventActor<E> 
        implements EventDestination<E> {

    /**
     * The actor's mailbox.
     */
    private EventQueue<E> eventQueue;

    /**
     * Handles callbacks from the mailbox.
     */
    private ActiveEventProcessor<E> eventProcessor = new ActiveEventProcessor<E>() {
        @Override
        public void haveEvents() {
            eventQueue.dispatchEvents();
        }

        @Override
        public void processEvent(E event) {
            JAEventActor.this.processEvent(event);
        }
    };

    /**
     * Create a JAEventActor
     *
     * @param threadManager Provides a thread for processing dispatched events.
     */
    public JAEventActor(ThreadManager threadManager) {
        this(new JAEventQueue<E>(threadManager));
    }

    /**
     * Create a JAEventActor
     *
     * @param eventQueue The actor's mailbox.
     */
    public JAEventActor(EventQueue<E> eventQueue) {
        this.eventQueue = eventQueue;
        eventQueue.setEventProcessor(eventProcessor);
    }

    /**
     * Send an event.
     *
     * @param destination Where the event is to be sent.
     * @param event The event to be sent.
     */
    public final void send(EventDestination<E> destination, E event) {
        destination.putEvent(event);
    }

    /**
     * The putEvent method adds an events to the queue of events to be processed.
     *
     * @param event The events to be processed.
     */
    @Override
    public final void putEvent(E event) {
        eventQueue.putEvent(event);
    }

    /**
     * The processMessage method is called when there is an incoming event to process.
     *
     * @param event The event to be processed.
     */
    abstract protected void processEvent(E event);
}
