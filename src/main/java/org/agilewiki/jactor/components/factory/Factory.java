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
package org.agilewiki.jactor.components.factory;

import org.agilewiki.jactor.Actor;
import org.agilewiki.jactor.Mailbox;
import org.agilewiki.jactor.bind.ConcurrentMethodBinding;
import org.agilewiki.jactor.bind.Internals;
import org.agilewiki.jactor.bind.RequestReceiver;
import org.agilewiki.jactor.bind.VoidInitializationMethodBinding;
import org.agilewiki.jactor.components.Component;
import org.agilewiki.jactor.components.Include;
import org.agilewiki.jactor.components.JCActor;

import java.lang.reflect.Constructor;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * <p>A component for defining actor types and creating instances,
 * where an actor type has a name and a class.</p>
 */
public class Factory extends Component {
    /**
     * A table which maps type names to classes.
     */
    private ConcurrentSkipListMap<String, Object> types = new ConcurrentSkipListMap<String, Object>();

    /**
     * Bind request classes.
     *
     * @throws Exception Any exceptions thrown while binding.
     */
    @Override
    public void bindery() throws Exception {
        super.bindery();

        thisActor.bind(
                DefineActorType.class.getName(),
                new VoidInitializationMethodBinding<DefineActorType>() {
                    @Override
                    public void initializationProcessRequest(Internals internals, DefineActorType defineActorType)
                            throws Exception {
                        String actorType = defineActorType.getActorType();
                        if (types.containsKey(actorType))
                            throw new IllegalArgumentException("Actor type is already defined: " + actorType);
                        Class clazz = defineActorType.getClazz();
                        if (Component.class.isAssignableFrom(clazz)) {
                            types.put(actorType, clazz);
                            return;
                        }
                        if (Actor.class.isAssignableFrom(clazz)) {
                            types.put(actorType, clazz.getConstructor(Mailbox.class));
                            return;
                        }
                        throw new IllegalArgumentException(clazz.getName());
                    }
                });

        thisActor.bind(
                NewActor.class.getName(),
                new ConcurrentMethodBinding<NewActor, Actor>() {
                    @Override
                    public Actor concurrentProcessRequest(RequestReceiver requestReceiver,
                                                          NewActor request)
                            throws Exception {
                        String actorType = request.getActorType();
                        Mailbox mailbox = request.getMailbox();
                        Actor parent = request.getParent();
                        if (mailbox == null || parent == null) {
                            if (mailbox == null) mailbox = requestReceiver.getMailbox();
                            if (parent == null) parent = requestReceiver.getThisActor();
                            request = new NewActor(actorType, mailbox, parent);
                        }
                        Object c = types.get(actorType);
                        if (c == null) {
                            if (parentHasSameComponent())
                                return request.call(requestReceiver.getParent());
                            throw new IllegalArgumentException("Unknown actor type: " + actorType);
                        }
                        if (c instanceof Class) {
                            Class clazz = (Class) c;
                            Include include = new Include(clazz);
                            JCActor actor = new JCActor(mailbox);
                            actor.setActorType(actorType);
                            actor.setParent(parent);
                            include.call(actor);
                            return actor;
                        }
                        Constructor constructor = (Constructor) c;
                        Actor a = (Actor) constructor.newInstance(mailbox);
                        a.setActorType(actorType);
                        a.setParent(parent);
                        return a;
                    }
                });
    }
}
