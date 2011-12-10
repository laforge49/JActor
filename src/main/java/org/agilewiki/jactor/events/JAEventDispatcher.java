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

import org.agilewiki.jactor.concurrent.ConcurrentLinkedBlockingQueue;
import org.agilewiki.jactor.concurrent.ThreadManager;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * An JAEventDispatcher receives messages, queues them,
 * and then processes them on another thread.
 */
final public class JAEventDispatcher<E> implements EventDispatcher<E> {
    /**
     * Provides a thread for processing dispatched events.
     */
    private ThreadManager threadManager;

    /**
     * Process the dispatched events.
     */
    private EventProcessor<E> eventProcessor;

    /**
     * A queue of pending events.
     */
    private ConcurrentLinkedBlockingQueue<E> queue = new ConcurrentLinkedBlockingQueue<E>();

    /**
     * Set to true when busy.
     */
    private AtomicBoolean running = new AtomicBoolean();

    /**
     * The events being dispatched.
     */
    private E event;

    /**
     * The task is used to process the events in the queue.
     * Each events is in turn processed using the JAEventDispatcher.
     */
    private Runnable task = new Runnable() {
        @Override
        public void run() {
            while (true) {
                event = queue.poll();
                if (event == null) {
                    running.set(false);
                    if (queue.peek() == null || !running.compareAndSet(false, true))
                        return;
                }
                eventProcessor.haveEvents();
            }
        }
    };

    /**
     * Creates a JAEventDispatcher.
     *
     * @param threadManager Provides a thread for processing dispatched events.
     */
    public JAEventDispatcher(ThreadManager threadManager) {
        this.threadManager = threadManager;
    }

    /**
     * Specifies the object which will process the dispatched events.
     * @param eventProcessor Processes the dispatched events.
     */
    @Override
    public void setEventProcessor(EventProcessor<E> eventProcessor) {
        this.eventProcessor = eventProcessor;
    }

    /**
     * The isEmpty method returns true when there are no pending events,
     * though the results may not always be correct due to concurrency issues.
     */
    @Override
    public boolean isEmpty() {
        return queue.peek() == null;
    }

    /**
     * The put method adds an events to the queue of events to be processed.
     *
     * @param event The events to be processed.
     */
    @Override
    public void put(E event) {
        queue.put(event);
        if (running.compareAndSet(false, true)) {
            threadManager.process(task);
        }
    }

    /**
     * The dispatchEvents method processes any events in the queue.
     * True is returned if any events were actually processed.
     */
    @Override
    public boolean dispatchEvents() {
        if (event == null) event = queue.poll();
        if (event == null) return false;
        while (event != null) {
            E e = event;
            event = null;
            eventProcessor.processEvent(e);
            event = queue.poll();
        }
        return true;
    }
}
