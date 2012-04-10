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
package org.agilewiki.jactor.pubsub.actorName;

import org.agilewiki.jactor.Mailbox;
import org.agilewiki.jactor.RP;
import org.agilewiki.jactor.lpc.JLPCActor;

/**
 * Implements immutable actor object names.
 * Supported request messages: SetActorName and GetActorName.
 */
public class JActorName extends JLPCActor implements ActorName {
    /**
     * The actor name, or null.
     */
    private String actorName;

    /**
     * Create a LiteActor
     *
     * @param mailbox A mailbox which may be shared with other actors.
     */
    public JActorName(Mailbox mailbox) {
        super(mailbox);
    }

    /**
     * Returns the actor name, or null.
     *
     * @return The actor name, or null.
     */
    @Override
    public String getActorName() {
        return actorName;
    }

    /**
     * Assigns an actor name, unless already assigned.
     * (Not thread safe!)
     *
     * @param actorName The actor name.
     */
    @Override
    public void setActorName(String actorName) throws Exception {
        if (this.actorName != null)
            throw new UnsupportedOperationException("Already named: " + this.actorName);
        this.actorName = actorName;
    }

    /**
     * The application method for processing requests sent to the actor.
     *
     * @param request A request.
     * @param rp      The response processor.
     * @throws Exception Any uncaught exceptions raised while processing the request.
     */
    @Override
    protected void processRequest(Object request, RP rp) throws Exception {
        Class reqcls = request.getClass();

        if (reqcls == GetActorName.class) {
            rp.processResponse(getActorName());
            return;
        }

        throw new UnsupportedOperationException(reqcls.getName());
    }
}
