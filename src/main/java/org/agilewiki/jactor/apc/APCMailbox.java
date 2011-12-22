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
package org.agilewiki.jactor.apc;

import org.agilewiki.jactor.bufferedEvents.BufferedEventsDestination;

/**
 * Serves as the asynchronous transport for APCMessages.
 */
public interface APCMailbox extends BufferedEventsDestination<JAPCMessage> {

    /**
     * Returns the request message being processed.
     *
     * @return The request message being processed.
     */
    public JAPCRequest getCurrentRequest();

    /**
     * Assigns the request message to be processed.
     *
     * @param currentRequest The request message being processed.
     */
    public void setCurrentRequest(JAPCRequest currentRequest);

    /**
     * Send any pending messages.
     */
    public void sendPendingMessages();

    /**
     * Set the initial capacity for buffered outgoing messages.
     *
     * @param initialBufferCapacity The initial capacity for buffered outgoing messages.
     */
    public void setInitialBufferCapacity(int initialBufferCapacity);

    /**
     * Buffer the request for subsequent sending.
     *
     * @param destination Buffered events receiver.
     * @param request     The request to be sent.
     */
    public void apcSend(BufferedEventsDestination<JAPCMessage> destination, JAPCRequest request);

    /**
     * Return the response for processing.
     *
     * @param unwrappedResponse
     */
    public void response(Object unwrappedResponse);

    /**
     * The isEmpty method returns true when there are no pending messages,
     * though the results may not always be correct due to concurrency issues.
     */
    public boolean isEmpty();

    /**
     * The dispatchMessages method processes any messages in the queue.
     * True is returned if any messages were actually processed.
     */
    public boolean dispatchEvents();
}
