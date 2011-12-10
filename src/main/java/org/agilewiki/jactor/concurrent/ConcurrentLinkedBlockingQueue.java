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
package org.agilewiki.jactor.concurrent;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * A ConcurrentLinkedQueue with a take method that doesn't block when the queue isn't empty.
 * Note that this code only supports a singe reader thread.
 *
 * @param <E> The type of element.
 */
final public class ConcurrentLinkedBlockingQueue<E> extends ConcurrentLinkedQueue<E> {
    /**
     * When true, take is requesting a permit.
     */
    private AtomicBoolean waiting = new AtomicBoolean();

    /**
     * To wake up a pending take.
     */
    private Semaphore wakeup = new Semaphore(0);

    /**
     * Inserts the element at the tail of the queue.
     *
     * @param e The element to be added
     */
    public void put(E e) {
        offer(e);
    }

    /**
     * Inserts the element at the tail of the queue.
     * As the queue is unbounded, this method will never return {@code false}.
     *
     * @param e The element to be added
     */
    @Override
    public boolean offer(E e) {
        super.offer(e);
        if (waiting.compareAndSet(true, false)) wakeup.release(); //if there is a pending take, wake it up
        return true;
    }

    /**
     * Returns the element at head of the queue when an element is available.
     * This method is similar to poll, except that it does not return null.
     */
    public E take() {
        while (true) {
            E rv = poll();
            if (rv != null) return rv;
            //the queue may now be empty, so request a permit
            waiting.set(true);
            rv = poll();
            if (rv != null) {
                //the queue was not empty
                if (!waiting.compareAndSet(true, false))
                    wakeup.drainPermits(); //clear the permit that we didn't need
                return rv;
            }
            try {
                wakeup.acquire(); //wait for a permit
            } catch (InterruptedException e) {
            }
        }
    }
}
