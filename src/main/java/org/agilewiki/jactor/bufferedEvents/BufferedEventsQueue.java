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

import org.agilewiki.jactor.events.EventDispatcher;
import org.agilewiki.jactor.events.EventQueue;

import java.util.List;

/**
 * A BufferedEventsQueue receives buffered events, queues them,
 * and then processes them on another thread.
 *
 * @param <E> The type of event.
 */
public interface BufferedEventsQueue<E>
        extends BufferedEventsDestination<E>, EventDispatcher<E> {
    /**
     * Send any pending events.
     */
    public void sendPendingEvents();

    /**
     * Set the initial capacity for buffered outgoing events.
     *
     * @param initialBufferCapacity The initial capacity for buffered outgoing events.
     */
    public void setInitialBufferCapacity(int initialBufferCapacity);

    /**
     * Buffer the event for subsequent sending.
     *
     * @param destination Buffered events receiver.
     * @param event       The event to be sent.
     */
    public void send(BufferedEventsDestination<E> destination, E event);

    /**
     * Returns the event queue.
     *
     * @return The event queue.
     */
    public EventQueue<List<E>> getEventQueue();
}
