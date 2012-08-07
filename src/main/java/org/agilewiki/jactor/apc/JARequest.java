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

import org.agilewiki.jactor.ExceptionHandler;
import org.agilewiki.jactor.Mailbox;
import org.agilewiki.jactor.RP;
import org.agilewiki.jactor.bufferedEvents.BufferedEventsQueue;
import org.agilewiki.jactor.lpc.JLPCActor;
import org.agilewiki.jactor.lpc.Request;
import org.agilewiki.jactor.lpc.RequestSource;

/**
 * Requests sent to a JAPCMailbox are wrapped by an JARequest.
 */
public abstract class JARequest extends RP implements JAMessage {

    public Mailbox mailbox;

    /**
     * The target of the response.
     * An anonymous object in the JLPCActor or JLPCFuture
     * that originated the request. Used in response processing.
     */
    protected APCRequestSource requestSource;

    /**
     * An anonymous object in the JLPCActor that is the target of the JARequest.
     */
    private JLPCActor destinationActor;

    /**
     * The unwrapped request that was sent to a JLPCActor.
     */
    private Request unwrappedRequest;

    /**
     * Initially true, active is set to false when a response is sent.
     * Used to prevent multiple responses to a request.
     */
    private boolean active = true;

    public Mailbox sourceMailbox;

    public JARequest sourceRequest;

    public ExceptionHandler sourceExceptionHandler;

    public RP rp;

    protected final void reset() {
        mailbox = null;
        requestSource = null;
        destinationActor = null;
        unwrappedRequest = null;
        sourceMailbox = null;
        sourceRequest = null;
        sourceExceptionHandler = null;
        rp = null;
    }

    public JARequest(RequestSource requestSource,
                     JLPCActor destinationActor,
                     Request unwrappedRequest,
                     RP rp,
                     Mailbox mailbox) {
        this.mailbox = mailbox;
        this.requestSource = requestSource;
        this.destinationActor = destinationActor;
        this.unwrappedRequest = unwrappedRequest;
        this.rp = rp;
        sourceMailbox = requestSource.getMailbox();
        if (sourceMailbox != null) {
            sourceRequest = sourceMailbox.getCurrentRequest();
            sourceExceptionHandler = sourceMailbox.getExceptionHandler();
        } else {
            sourceRequest = null;
            sourceExceptionHandler = null;
        }
    }

    /**
     * Returns the requestProcessor.
     *
     * @return The requestProcessor.
     */
    final public JLPCActor getDestinationActor() {
        return destinationActor;
    }

    /**
     * Returns the unwrapped request.
     *
     * @return The unwrapped request.
     */
    final public Request getUnwrappedRequest() {
        return unwrappedRequest;
    }

    /**
     * Returns true if no response has been returned.
     *
     * @return Is true when no response has been returned.
     */
    final public boolean isActive() {
        return active;
    }

    /**
     * Sets active to false--a response has been returned.
     */
    final public void inactive() {
        active = false;
        mailbox.setCurrentRequest(null);
    }

    /**
     * Enqueue a response to be sent when there are no more incoming messages to be processed.
     *
     * @param eventQueue        The internal queue used by JLPCMailbox.
     * @param unwrappedResponse The unwrapped response.
     */
    final public void response(BufferedEventsQueue<JAMessage> eventQueue, Object unwrappedResponse) {
        JAResponse japcResponse = new JAResponse(unwrappedResponse);
        japcResponse.setJAPCRequest(this);
        requestSource.responseFrom(eventQueue, japcResponse);
    }

    public void restoreSourceMailbox() {
        if (sourceMailbox != null) {
            sourceMailbox.setCurrentRequest(sourceRequest);
            sourceMailbox.setExceptionHandler(sourceExceptionHandler);
        }
    }
}
