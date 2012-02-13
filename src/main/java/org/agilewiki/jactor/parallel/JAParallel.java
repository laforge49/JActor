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
package org.agilewiki.jactor.parallel;

import org.agilewiki.jactor.Actor;
import org.agilewiki.jactor.Mailbox;
import org.agilewiki.jactor.RP;
import org.agilewiki.jactor.lpc.JLPCActor;

/**
 * Supports parallel request processing.
 */
final public class JAParallel extends JLPCActor {
    /**
     * The actors to be run in parallel.
     */
    private Actor[] actors;

    /**
     * Returns a response only when the expected number of responses are received.
     */
    private JAResponseCounter responseCounter;

    /**
     * Create a JAParallel actor.
     *
     * @param mailbox A mailbox which may be shared with other actors.
     * @param actors  The actors to be run in parallel.
     */
    public JAParallel(Mailbox mailbox, Actor[] actors) {
        super(mailbox);
        this.actors = actors;
        int p = actors.length;
    }

    /**
     * Sends either the same request to all the actors or a different request to each actor.
     *
     * @param request The request or an array of requests.
     * @param rd1     Process the response sent when responses from all the actors have been received.
     * @throws Exception Any uncaught exception thrown when the request is processed.
     */
    @Override
    public void processRequest(final Object request, final RP rd1)
            throws Exception {
        int p = actors.length;
        responseCounter = new JAResponseCounter(p, rd1);
        int i = 0;
        if (request instanceof Object[]) {
            Object[] requests = (Object[]) request;
            if (requests.length != p)
                throw new IllegalArgumentException("Request and actor arrays not the same length");
            while (i < p) {
                send(actors[i], requests[i], responseCounter);
                i += 1;
            }
        } else while (i < p) {
            send(actors[i], request, responseCounter);
            i += 1;
        }
    }
}
