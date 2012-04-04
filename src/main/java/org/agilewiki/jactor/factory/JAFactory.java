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
import org.agilewiki.jactor.RP;
import org.agilewiki.jactor.lpc.JLPCActor;

import java.lang.reflect.Constructor;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * An actor for defining actor types and creating instances.
 */
public class JAFactory extends JLPCActor {
    /**
     * A table which maps type names to actor factories.
     */
    private ConcurrentSkipListMap<String, ActorFactory> types = new ConcurrentSkipListMap<String, ActorFactory>();

    /**
     * Create a LiteActor
     *
     * @param mailbox A mailbox which may be shared with other actors.
     */
    public JAFactory(Mailbox mailbox) {
        super(mailbox);
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
        if (request instanceof NewActor) {
            rp.processResponse(newActor((NewActor) request));
            return;
        }
        if (request instanceof GetActorFactory) {
            rp.processResponse(getActorFactory((GetActorFactory) request));
            return;
        }
        if (request instanceof DefineActorType) {
            DefineActorType req = (DefineActorType) request;
            defineActorType(req.getActorType(), req.getClazz());
            rp.processResponse(null);
            return;
        }
        if (request instanceof RegisterActorFactory) {
            RegisterActorFactory req = (RegisterActorFactory) request;
            registerActorFactory(req.getActorType(), req.getActorFactory());
            rp.processResponse(null);
            return;
        }
    }

    protected Actor newActor(NewActor request)
            throws Exception {
        String actorType = request.getActorType();
        Mailbox mailbox = request.getMailbox();
        Actor parent = request.getParent();
        if (mailbox == null || parent == null) {
            if (mailbox == null) mailbox = getMailbox();
            if (parent == null) parent = getThisActor();
            request = new NewActor(actorType, mailbox, parent);
        }
        ActorFactory af = types.get(actorType);
        if (af == null) {
            Actor a = request.getTargetActor(parent);
            if (a != null)
                return request.call(a);
            throw new IllegalArgumentException("Unknown actor type: " + actorType);
        }
        return af.newActor(mailbox, parent);
    }

    protected ActorFactory getActorFactory(GetActorFactory request)
            throws Exception {
        String actorType = request.getActorType();
        ActorFactory af = types.get(actorType);
        if (af == null) {
            Actor a = request.getTargetActor(JAFactory.this.getParent());
            if (a != null)
                return request.call(a);
            throw new IllegalArgumentException("Unknown actor type: " + actorType);
        }
        return af;
    }

    protected void defineActorType(String actorType, Class clazz)
            throws Exception {
        if (types.containsKey(actorType))
            throw new IllegalArgumentException("Actor type is already defined: " + actorType);
        if (Actor.class.isAssignableFrom(clazz)) {
            Constructor componentConstructor = clazz.getConstructor(Mailbox.class);
            types.put(actorType, new _ActorFactory(actorType, componentConstructor));
            return;
        }
        throw new IllegalArgumentException(clazz.getName());
    }

    protected void registerActorFactory(String actorType, ActorFactory actorFactory)
            throws Exception {
        if (types.containsKey(actorType))
            throw new IllegalArgumentException("Actor type is already defined: " + actorType);
        types.put(actorType, actorFactory);
    }
}
