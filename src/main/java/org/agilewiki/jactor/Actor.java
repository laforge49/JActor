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
package org.agilewiki.jactor;

import org.agilewiki.jactor.apc.APCRequestSource;
import org.agilewiki.jactor.lpc.JLPCActor;
import org.agilewiki.jactor.lpc.Request;

/**
 * <p>
 * Actors are objects which send messages to each other and which process only one message at a time. Messages are
 * either requests or responses. When an actor sends a request to another actor, it expects to receive a single
 * response unless an exception has been thrown.
 * </p><p>
 * When an actor sends a message it provides a callback to handle the response. The callback may be invoked either
 * immediately or later. After sending a message, an actor may send additional messages or return control, at which
 * point it may receive other requests or pending responses.
 * </p><p>
 * An actor typically interleaves the processing of multiple requests. To process a request to completion, an actor may
 * need to send requests to other actors (in series or parallel) and process the responses to those requests. Actors
 * process requests and responses as they are received whenever the actor is not busy processing a message. There is no
 * way to block incoming requests. Actors are thread-safe, but message processing is not atomic.
 * </p><p>
 * Some actors are asynchronous. Requests sent to an asynchronous actor are processed on a different thread. Actors which
 * perform heavy computation or which do I/O should be asynchronous. Asynchronous actors play an important role in
 * vertical scalability, allowing a program to make effective use of multiple hardware threads. But care should be used,
 * as asynchronous message passing tends to be slow.
 * </p>
 */
public interface Actor {
    /**
     * Wraps and enqueues an unwrapped request in the requester's inbox.
     *
     * @param requestSource The originator of the request.
     * @param request       The unwrapped request to be sent.
     * @param rp            The request processor.
     */
    public void acceptRequest(APCRequestSource requestSource,
                              Request request,
                              RP rp)
            throws Exception;

    /**
     * Wraps and enqueues an unwrapped request in the requester's inbox.
     *
     * @param requestSource The originator of the request.
     * @param request       The unwrapped request to be sent.
     */
    public void acceptEvent(APCRequestSource requestSource,
                            Request request)
            throws Exception;

    /**
     * Set the initial capacity for buffered outgoing messages.
     *
     * @param initialBufferCapacity The initial capacity for buffered outgoing messages.
     */
    public void setInitialBufferCapacity(int initialBufferCapacity);

    /**
     * Returns true when the concurrent data of the actor, or its parent, contains the named data item.
     *
     * @param name The key for the data item.
     * @return True when the concurrent data of the actor, or its parent, contains the named data item.
     */
    public boolean hasDataItem(String name);

    /**
     * Returns the actor's mailbox.
     *
     * @return The actor's mailbox.
     */
    public Mailbox getMailbox();

    /**
     * Returns the actor's parent.
     *
     * @return The actor's parent, or null.
     */
    public JLPCActor getParent();

    /**
     * Returns A matching ancestor from the parent chain.
     *
     * @param targetClass A class which the ancestor is an instanceof.
     * @return The matching ancestor, or null.
     */
    public JLPCActor getAncestor(Class targetClass);

    public JLPCActor getMatch(Class targetClass);
}
