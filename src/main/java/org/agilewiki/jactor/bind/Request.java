/*
 * Copyright 2012 Bill La Forge
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
package org.agilewiki.jactor.bind;

import org.agilewiki.jactor.Actor;
import org.agilewiki.jactor.JANoResponse;
import org.agilewiki.jactor.apc.APCRequestSource;

/**
 * A JBActor request.
 */
public class Request<RESPONSE_TYPE> {
    /**
     * Send a request.
     *
     * @param senderInternals The sending actor's internals.
     * @param targetActor     The target actor.
     * @param rp              The response processor.
     * @throws Exception Any uncaught exceptions raised while processing the request.
     */
    public void send(Internals senderInternals, Actor targetActor, RP<RESPONSE_TYPE> rp)
            throws Exception {
        senderInternals.send(targetActor, this, rp);
    }

    /**
     * Send a request.
     *
     * @param requestSource The sender of the request.
     * @param targetActor   The target actor.
     * @param rp            The response processor.
     * @throws Exception Any uncaught exceptions raised while processing the request.
     */
    public void send(APCRequestSource requestSource, Actor targetActor, RP<RESPONSE_TYPE> rp)
            throws Exception {
        targetActor.acceptRequest(requestSource, this, rp);
    }

    /**
     * Send a request event.
     *
     * @param senderInternals The sending actor's internals.
     * @param targetActor     The target actor.
     * @throws Exception Any uncaught exceptions raised while processing the request.
     */
    public void sendEvent(Internals senderInternals, Actor targetActor)
            throws Exception {
        senderInternals.sendEvent(targetActor, this);
    }

    /**
     * Send a request event.
     *
     * @param requestSource The sender of the request.
     * @param targetActor   The target actor.
     * @throws Exception Any uncaught exceptions raised while processing the request.
     */
    public void sendEvent(APCRequestSource requestSource, Actor targetActor)
            throws Exception {
        targetActor.acceptRequest(requestSource, this, JANoResponse.nrp);
    }
}
