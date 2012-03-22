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
package org.agilewiki.jactor.lpc;

import org.agilewiki.jactor.Actor;
import org.agilewiki.jactor.bind.JBActor;

/**
 * A request that can be passed synchronously to a JBActor for processing,
 * but only until the actor is initialized.
 */
abstract public class InitializationRequest<RESPONSE_TYPE, TARGET_TYPE>
        extends ExternallyCallableRequest<RESPONSE_TYPE, TARGET_TYPE> {
    /**
     * Send an initialization request.
     *
     * @param targetActor The target actor.
     * @return The response.
     * @throws Exception Any uncaught exceptions raised while processing the request.
     */
    public RESPONSE_TYPE call(JBActor targetActor)
            throws Exception {
        return (RESPONSE_TYPE) targetActor.acceptCall(this);
    }

    /**
     * Send an initialization request.
     *
     * @param targetActor The target actor.
     * @return The response.
     * @throws Exception Any uncaught exceptions raised while processing the request.
     */
    public RESPONSE_TYPE call(TARGET_TYPE targetActor)
            throws Exception {
        throw new UnsupportedOperationException();
    }

    /**
     * Send an initialization request.
     *
     * @param targetActor The target actor.
     * @return The response.
     * @throws Exception Any uncaught exceptions raised while processing the request.
     */
    final public RESPONSE_TYPE call(Actor targetActor)
            throws Exception {
        if (targetActor instanceof JBActor)
            return call((JBActor) targetActor);
        if (isTargetType(targetActor))
            return call((TARGET_TYPE) targetActor);
        Actor parent = targetActor.getParent();
        if (parent != null)
            return call(parent);
        throw new UnsupportedOperationException();
    }
}
