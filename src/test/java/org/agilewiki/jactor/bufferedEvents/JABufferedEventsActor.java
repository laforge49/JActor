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
import org.agilewiki.jactor.events.JActor;

import java.util.ArrayList;

/**
 * Test code: Implements BufferedEventsActor.
 *
 * @param <E> The type of event.
 */
abstract public class JABufferedEventsActor<E> extends JActor<E>
        implements BufferedEventsActor<E> {

    /**
     * The actor's mailbox.
     */
    private BufferedEventsQueue<E> mailbox;

    /**
     * Create a JAEventActor
     *
     * @param threadManager Provides a thread for processing dispatched events.
     */
    public JABufferedEventsActor(ThreadManager threadManager) {
        this(new JABufferedEventsQueue<E>(threadManager, true));
    }

    /**
     * Create a JAEventActor
     * Use this constructor when providing an implementation of BufferedEventsQueue
     * other than JABufferedEventsQueue.
     *
     * @param mailbox The actor's mailbox.
     */
    public JABufferedEventsActor(BufferedEventsQueue<E> mailbox) {
        super(mailbox);
        this.mailbox = mailbox;
    }

    /**
     * Set the initial capacity for buffered outgoing events.
     *
     * @param initialBufferCapacity The initial capacity for buffered outgoing events.
     */
    final public void setInitialBufferCapacity(int initialBufferCapacity) {
        mailbox.setInitialBufferCapacity(initialBufferCapacity);
    }

    /**
     * Buffer the event for subsequent sending.
     *
     * @param destination Buffered events receiver.
     * @param event       The event to be sent.
     */
    final protected void send(BufferedEventsDestination<E> destination, E event) {
        mailbox.send(destination, event);
    }

    /**
     * The putBufferedEvents method adds events to be processed.
     *
     * @param bufferedEvents The events to be processed.
     */
    @Override
    final public void putBufferedEvents(ArrayList<E> bufferedEvents) {
        mailbox.putBufferedEvents(bufferedEvents);
    }
}
