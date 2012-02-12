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

import java.util.concurrent.atomic.AtomicReference;

/**
 * An JAEventQueue receives messages, queues them,
 * and then processes them on another thread.
 *
 * @param <E> The type of event.
 */
final public class JAEventQueue<E> implements EventQueue<E> {
    private volatile boolean notEmpty;

    /**
     * Provides a thread for processing dispatched events.
     */
    private ThreadManager threadManager;

    /**
     * Process the dispatched events.
     */
    private ActiveEventProcessor<E> eventProcessor;

    /**
     * A queue of pending events.
     */
    private ConcurrentLinkedBlockingQueue<E> queue = new ConcurrentLinkedBlockingQueue<E>();

    /**
     * Set to true when busy.
     */
    private AtomicReference<JAEventQueue<E>> atomicControl = new AtomicReference<JAEventQueue<E>>();

    /**
     * The task is used to process the events in the queue.
     * Each events is in turn processed using the JAEventQueue.
     */
    private Runnable task = new Runnable() {
        @Override
        public void run() {
            if (atomicControl.compareAndSet(null, getController()))
                while (true) {
                    E event = queue.peek();
                    if (event == null) {
                        atomicControl.set(null);
                        if (queue.peek() == null || !atomicControl.compareAndSet(null, getController()))
                            return;
                    }
                    notEmpty = false;
                    eventProcessor.haveEvents();
                }
        }
    };

    /**
     * Creates a JAEventQueue.
     *
     * @param threadManager Provides a thread for processing dispatched events.
     */
    public JAEventQueue(ThreadManager threadManager) {
        this.threadManager = threadManager;
    }

    /**
     * Gain control of the queue.
     *
     * @param eventQueue A queue.
     * @return True when control was acquired.
     */
    public boolean acquireControl(EventQueue<E> eventQueue) {
        if (atomicControl.compareAndSet(null, eventQueue.getController())) {
            notEmpty = false;
            return true;
        }
        return false;
    }

    /**
     * Relinquish foreign control over the queue.
     */
    public void relinquishControl() {
        JAEventQueue<E> c = atomicControl.get();
        if (c == this)
            return;
        atomicControl.set(null);
        if (notEmpty) {
            threadManager.process(task);
        }
    }

    /**
     * Returns the controlling queue.
     *
     * @return The controlling queue.
     */
    public JAEventQueue<E> getController() {
        JAEventQueue<E> c = atomicControl.get();
        if (c == null)
            return this;
        return c;
    }

    /**
     * Specifies the object which will process the dispatched events.
     *
     * @param eventProcessor Processes the dispatched events.
     */
    @Override
    public void setEventProcessor(ActiveEventProcessor<E> eventProcessor) {
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
     * The putEvent method adds an events to the queue of events to be processed
     * and if idle, start the task.
     *
     * @param event The events to be processed.
     */
    @Override
    public void putEvent(E event) {
        queue.put(event);
        notEmpty = true;
        if (atomicControl.get() == null)
            threadManager.process(task);
    }

    /**
     * The dispatchEvents method processes any events in the queue.
     * True is returned if any events were actually processed.
     */
    @Override
    public boolean dispatchEvents() {
        E event = queue.poll();
        if (event == null) return false;
        while (event != null) {
            eventProcessor.processEvent(event);
            event = queue.poll();
        }
        return true;
    }
}
