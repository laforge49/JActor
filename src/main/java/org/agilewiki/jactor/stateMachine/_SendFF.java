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
package org.agilewiki.jactor.stateMachine;

import org.agilewiki.jactor.Actor;

/**
 * Send a request to an actor.
 */
final public class _SendFF extends _Send {
    /**
     * The (indirect) actor which is to receive the message.
     */
    private ActorFunc targetActor;

    /**
     * The (indirect) request.
     */
    private ObjectFunc request;

    /**
     * The name of the result, or null.
     */
    private String resultName;

    /**
     * Create a _SendFF.
     *
     * @param parentSMB The parent builder.
     * @param targetActor The (indirect) actor which is to receive the message.
     * @param request The (indirect) request.
     * @param resultName The name of the result, or null.
     */
    public _SendFF(_SMBuilder parentSMB, ActorFunc targetActor, ObjectFunc request, String resultName) {
        this.targetActor = targetActor;
        this.request = request;
        this.resultName = resultName;
        parentSMB.add(this);
    }

    /**
     * Returns the actor which is to receive the message.
     *
     * @return The actor which is to receive the message.
     */
    @Override
    public Actor getTargetActor() {
        return targetActor.get();
    }

    /**
     * Returns the request.
     *
     * @return The request.
     */
    @Override
    public Object getRequest() {
        return request.get();
    }

    /**
     * Returns the name of the result, or null.
     *
     * @return The name of the result, or null.
     */
    @Override
    public String getResultName() {
        return resultName;
    }
}
