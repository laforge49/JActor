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
import org.agilewiki.jactor.events.ActiveEventProcessor;

import java.util.ArrayList;

abstract public class JABufferedEventsActor<E>
        implements ActiveEventProcessor<E>, BufferedEventsDestination<E>, BufferedEventSource<E> {

    /**
     * The actor's mailbox.
     */
    private BufferedEventsQueue<E> eventQueue;

    /**
     * Create a JAEventActor
     *
     * @param threadManager Provides a thread for processing dispatched events.
     */
    public JABufferedEventsActor(ThreadManager threadManager) {
        eventQueue = new JABufferedEventsQueue<E>(threadManager);
        eventQueue.setEventProcessor(this);
    }

    /**
     * Create a JAEventActor
     *
     * @param eventQueue The actor's mailbox.
     */
    public JABufferedEventsActor(BufferedEventsQueue<E> eventQueue) {
        this.eventQueue = eventQueue;
        eventQueue.setEventProcessor(this);
    }

    /**
     * The haveEvents method is called when
     * there may be one or more pending events.
     */
    @Override
    public void haveEvents() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * Buffer the event for subsequent sending.
     *
     * @param destination Buffered events receiver.
     * @param event       The event to be sent.
     */
    @Override
    public void send(BufferedEventsDestination<E> destination, E event) {
        eventQueue.send(destination, event);
    }

    /**
     * The putBufferedEvents method adds events to be processed.
     *
     * @param bufferedEvents The events to be processed.
     */
    @Override
    public void putBufferedEvents(ArrayList<E> bufferedEvents) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
