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
package org.agilewiki.jactor.components.actorRegistry;

import org.agilewiki.jactor.bind.JBConcurrentRequest;
import org.agilewiki.jactor.pubsub.actorName.ActorName;

/**
 * <p>Sent to an actor with an ActorRegistry component to register an actor
 * which has been assigned an actor name.</p>
 * <p>Only one actor can be registered for a given name.</p>
 */
final public class RegisterActor extends JBConcurrentRequest<Object> {
    /**
     * The actor to be registered.
     */
    private ActorName actor;

    /**
     * Create a RegisterActor request.
     *
     * @param actor The actor to be registered.
     */
    public RegisterActor(ActorName actor) {
        this.actor = actor;
    }

    /**
     * Returns the actor to be registered.
     *
     * @return The actor to be registered.
     */
    public ActorName getActor() {
        return actor;
    }
}
