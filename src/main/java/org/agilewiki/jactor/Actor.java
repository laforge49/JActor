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

/**
 * <p>
 * An actor which implements Local Procedure Calls (LPC).
 * Actors need to implement the processRequest method.
 * </p>
 * <pre>
 * public class Multiply {
 *     public int a;
 *     public int b;
 * }
 *
 * public class Multiplier extends JLPCActor {
 *
 *     public Multiplier(Mailbox mailbox) {
 *         super(mailbox);
 *     }
 *
 *     protected void processRequest(Object req, ResponseProcessor rp)
 *             throws Exception {
 *         Multiply m = (Multiply) req;
 *         rp.process(new Integer(m.a * m.b));
 *     }
 * }
 * </pre>
 */
public interface Actor {

    /**
     * Wraps and enqueues an unwrapped request in the requester's outbox.
     *
     * @param requestSource The originator of the request.
     * @param request       The unwrapped request to be sent.
     * @param rd            The request processor.
     */
    void acceptRequest(APCRequestSource requestSource,
                       Object request,
                       ResponseProcessor rd)
            throws Exception;

    /**
     * Set the initial capacity for buffered outgoing messages.
     *
     * @param initialBufferCapacity The initial capacity for buffered outgoing messages.
     */
    public void setInitialBufferCapacity(int initialBufferCapacity);

    /**
     * The application method for processing requests sent to the actor.
     *
     * @param request           A request.
     * @param responseProcessor The response processor.
     * @throws Exception Any uncaught exceptions raised while processing the request.
     */
    public void processRequest(Object request, ResponseProcessor responseProcessor)
            throws Exception;
}
