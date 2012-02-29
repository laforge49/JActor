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

import org.agilewiki.jactor.concurrent.ThreadManager;
import org.agilewiki.jactor.events.EventProcessor;
import org.agilewiki.jactor.events.EventQueue;
import org.agilewiki.jactor.events.JAEventQueue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * A BufferedEventsQueue receives buffered events, queues them,
 * and then processes them on another thread.
 *
 * @param <E> The type of event.
 */
final public class JABufferedEventsQueue<E>
        implements BufferedEventsQueue<E> {
    /**
     * The eventQueue handles the actual dispatching of buffered events.
     */
    private EventQueue<ArrayList<E>> eventQueue;

    /**
     * The eventProcessor is used to process unblocked events.
     */
    EventProcessor<E> eventProcessor;

    /**
     * Used for buffering outgoing events.
     */
    private int initialBufferCapacity = 10;

    /**
     * The pending HashMap holds the buffered events which
     * have not yet been sent.
     */
    private HashMap<BufferedEventsDestination<E>, ArrayList<E>> pending =
            new HashMap<BufferedEventsDestination<E>, ArrayList<E>>();

    /**
     * Create a BufferedEventsQueue.
     *
     * @param eventQueue Handles the actual dispatching of buffered events.
     */
    public JABufferedEventsQueue(EventQueue<ArrayList<E>> eventQueue) {
        this.eventQueue = eventQueue;
        eventQueue.setActiveEventProcessor(new EventProcessor<ArrayList<E>>() {
            @Override
            public void processEvent(ArrayList<E> bufferedEvents) {
                int i = 0;
                while (i < bufferedEvents.size()) {
                    eventProcessor.processEvent(bufferedEvents.get(i));
                    i += 1;
                }
            }

            @Override
            public void haveEvents() {
                eventProcessor.haveEvents();
            }
        });
    }

    /**
     * Create a BufferedEventsQueue.
     *
     * @param threadManager Provides a thread for processing dispatched events.
     * @param autonomous    Inhibits the acquireControl operation when true.
     */
    public JABufferedEventsQueue(ThreadManager threadManager, boolean autonomous) {
        this(new JAEventQueue<ArrayList<E>>(threadManager, autonomous));
    }

    /**
     * Set the initial capacity for buffered outgoing events.
     *
     * @param initialBufferCapacity The initial capacity for buffered outgoing events.
     */
    @Override
    public void setInitialBufferCapacity(int initialBufferCapacity) {
        this.initialBufferCapacity = initialBufferCapacity;
    }

    /**
     * Buffer the event for subsequent sending.
     *
     * @param destination Buffered events receiver.
     * @param event       The event to be sent.
     */
    @Override
    public void send(BufferedEventsDestination<E> destination, E event) {
        ArrayList<E> bufferedEvents = pending.get(destination);
        if (bufferedEvents == null) {
            bufferedEvents = new ArrayList<E>(initialBufferCapacity);
            pending.put(destination, bufferedEvents);
        }
        bufferedEvents.add(event);
    }

    /**
     * Send any pending events.
     */
    public void sendPendingEvents() {
        if (isEmpty() && !pending.isEmpty()) {
            Iterator<BufferedEventsDestination<E>> it = pending.keySet().iterator();
            while (it.hasNext()) {
                BufferedEventsDestination<E> destination = it.next();
                ArrayList<E> bufferedEvents = pending.get(destination);
                destination.putBufferedEvents(bufferedEvents);
            }
            pending.clear();
        }
    }

    /**
     * The putBufferedEvents method adds events to be processed.
     *
     * @param bufferedEvents The events to be processed.
     */
    @Override
    public void putBufferedEvents(ArrayList<E> bufferedEvents) {
        eventQueue.putEvent(bufferedEvents);
    }

    /**
     * Specifies the object which will process the dispatched events.
     *
     * @param eventProcessor Processes the dispatched events.
     */
    @Override
    public void setActiveEventProcessor(EventProcessor<E> eventProcessor) {
        this.eventProcessor = eventProcessor;
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
        if (eventQueue.dispatchEvents()) {
            sendPendingEvents();
            return true;
        }
        return false;
    }

    /**
     * Returns the event queue.
     *
     * @return The event queue.
     */
    public EventQueue<ArrayList<E>> getEventQueue() {
        return eventQueue;
    }
}
