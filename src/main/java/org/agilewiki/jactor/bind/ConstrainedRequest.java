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
import org.agilewiki.jactor.apc.APCRequestSource;

/**
 * A request that can be passed to an actor for processing via the Internals.call or Actor.acceptCall methods.
 */
public class ConstrainedRequest<RESPONSE_TYPE> extends Request<RESPONSE_TYPE> {
    /**
     * Send a constrained request.
     * (Override this method for requests with known return types.)
     *
     * @param sourceInternals The internals of the sending actor.
     * @param targetActor     The target actor.
     * @return The response.
     * @throws Exception Any uncaught exceptions raised while processing the request.
     */
    public RESPONSE_TYPE call(Internals sourceInternals, Actor targetActor) throws Exception {
        return (RESPONSE_TYPE) sourceInternals.call(targetActor, this);
    }

    /**
     * Send a constrained request.
     * (Override this method for requests with known return types.)
     *
     * @param apcRequestSource The sender of the request.
     * @param targetActor      The target actor.
     * @return The response.
     * @throws Exception Any uncaught exceptions raised while processing the request.
     */
    public RESPONSE_TYPE call(APCRequestSource apcRequestSource, Actor targetActor)
            throws Exception {
        return (RESPONSE_TYPE) targetActor.acceptCall(apcRequestSource, this);
    }
}
