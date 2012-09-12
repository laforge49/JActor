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
package org.agilewiki.jactor.simpleMachine;

import org.agilewiki.jactor.Actor;
import org.agilewiki.jactor.RP;
import org.agilewiki.jactor.lpc.Request;

/**
 * Send a request to an actor.
 */
abstract public class _Send implements _Operation {
    /**
     * Perform the operation.
     *
     * @param stateMachine The state machine driving the operation.
     * @param rp           The response processor.
     * @throws Exception Any uncaught exceptions raised while performing the operation.
     */
    @Override
    final public void call(final SimpleMachine stateMachine, final RP rp) throws Exception {
        Actor a = getTargetActor(stateMachine);
        Request r = getRequest(stateMachine);
        stateMachine.send(a, r, new RP() {
            @Override
            final public void processResponse(Object response) throws Exception {
                String rn = getResultName();
                if (rn != null) stateMachine.put(rn, response);
                rp.processResponse(null);
            }
        });
    }

    /**
     * Returns the actor which is to receive the message.
     *
     * @param stateMachine The state machine.
     * @return The actor which is to receive the message.
     */
    abstract public Actor getTargetActor(SimpleMachine stateMachine) throws Exception;

    /**
     * Returns the request.
     *
     * @param stateMachine The state machine.
     * @return The request.
     */
    abstract public Request getRequest(SimpleMachine stateMachine);

    /**
     * Returns the name of the result, or null.
     *
     * @return The name of the result, or null.
     */
    abstract public String getResultName();
}
