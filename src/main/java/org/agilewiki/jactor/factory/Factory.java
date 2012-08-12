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
package org.agilewiki.jactor.factory;

import org.agilewiki.jactor.Actor;
import org.agilewiki.jactor.Mailbox;
import org.agilewiki.jactor.lpc.TargetActor;

/**
 * Defines actor types and instantiating
 */
public interface Factory extends TargetActor {
    /**
     * Bind an actor type to a Class.
     *
     * @param actorType The actor type.
     * @param clazz     The class of the actor.
     */
    public void defineActorType(String actorType, Class clazz)
            throws Exception;

    /**
     * Register an actor factory.
     *
     * @param actorFactory An actor factory.
     */
    public void registerActorFactory(ActorFactory actorFactory)
            throws Exception;

    /**
     * Returns the requested actor factory.
     *
     * @param actorType The actor type.
     * @return The registered actor factory.
     */
    public ActorFactory getActorFactory(String actorType)
            throws Exception;

    /**
     * Creates a new actor.
     *
     * @param actorType The actor type.
     * @return The new actor.
     */
    public Actor newActor(String actorType)
            throws Exception;

    /**
     * Creates a new actor.
     *
     * @param actorType The actor type.
     * @param mailbox   A mailbox which may be shared with other actors, or null.
     * @return The new actor.
     */
    public Actor newActor(String actorType, Mailbox mailbox)
            throws Exception;

    /**
     * Creates a new actor.
     *
     * @param actorType The actor type.
     * @param mailbox   A mailbox which may be shared with other actors, or null.
     * @param parent    The parent actor to which unrecognized requests are forwarded, or null.
     * @return The new actor.
     */
    public Actor newActor(String actorType, Mailbox mailbox, Actor parent)
            throws Exception;
}
