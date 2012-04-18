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
import org.agilewiki.jactor.lpc.ConcurrentRequest;

/**
 * <p>NewActor is a request to create and configure an actor.</p>
 * <p>If no mailbox is specified, the mailbox of the actor processing the request is used.
 * And if no parent is specified, the actor processing the request is used.</p>
 */
public class NewActor extends ConcurrentRequest<Actor, Factory> {
    /**
     * An actor type name.
     */
    private String actorType;

    /**
     * A mailbox which may be shared with other actors, or null.
     */
    private Mailbox mailbox;

    /**
     * The parent actor to which unrecognized requests are forwarded, or null.
     */
    private Actor parent;

    /**
     * Create a NewActor request.
     *
     * @param actorType An actor type name.
     */
    public NewActor(String actorType) {
        this(actorType, null);
    }

    /**
     * Create a NewActor request.
     *
     * @param actorType An actor type name.
     * @param mailbox   A mailbox which may be shared with other actors, or null.
     */
    public NewActor(String actorType, Mailbox mailbox) {
        this(actorType, mailbox, null);
    }

    /**
     * Create a NewActor request.
     *
     * @param actorType An actor type name.
     * @param mailbox   A mailbox which may be shared with other actors, or null.
     * @param parent    The parent actor to which unrecognized requests are forwarded, or null.
     */
    public NewActor(String actorType, Mailbox mailbox, Actor parent) {
        if (actorType == null) {
            throw new IllegalArgumentException("actorType may not be null");
        }
        this.actorType = actorType;
        this.mailbox = mailbox;
        this.parent = parent;
    }

    /**
     * Returns an actor type name.
     *
     * @return An actor type name.
     */
    public String getActorType() {
        return actorType;
    }

    /**
     * Returns a mailbox which may be shared with other actors.
     *
     * @return A mailbox which may be shared with other actors, or null.
     */
    public Mailbox getMailbox() {
        return mailbox;
    }

    /**
     * Returns the parent actor to which unrecognized requests are forwarded.
     *
     * @return The parent actor to which unrecognized requests are forwarded, or null.
     */
    public Actor getParent() {
        return parent;
    }

    /**
     * Send a synchronous request.
     *
     * @param targetActor The target actor.
     * @return The response.
     * @throws Exception Any uncaught exceptions raised while processing the request.
     */
    @Override
    protected Actor _call(Factory targetActor)
            throws Exception {
        return targetActor.newActor(this);
    }

    /**
     * Returns true when targetActor is an instanceof TARGET_TYPE
     *
     * @param targetActor The actor to be called.
     * @return True when targetActor is an instanceof TARGET_TYPE.
     */
    public boolean isTargetType(Actor targetActor) {
        return targetActor instanceof Factory;
    }
}
