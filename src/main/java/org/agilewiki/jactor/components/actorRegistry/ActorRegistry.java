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

import org.agilewiki.jactor.Actor;
import org.agilewiki.jactor.bind.ConcurrentMethodBinding;
import org.agilewiki.jactor.bind.RequestReceiver;
import org.agilewiki.jactor.bind.VoidConcurrentMethodBinding;
import org.agilewiki.jactor.components.Component;
import org.agilewiki.jactor.pubsub.actorName.ActorName;
import org.agilewiki.jactor.pubsub.actorName.GetActorName;

import java.util.concurrent.ConcurrentSkipListMap;

/**
 * <p>
 * Implements a register of actors which have the ActorName component.
 * Supported request messages: RegisterActor, UnregisterActor and GetRegisteredActor.
 * </p><p>
 * GetRegisteredActor first checks the component's own registry. If the actor is not
 * found and its parent also has an ActorRegistry, then the request is passed up to
 * the parent.
 * </p><p>
 * When the ActorRegistry is closed, close is called on all the registered actors.
 * </p>
 */
public class ActorRegistry extends Component {
    /**
     * Table of registered actors.
     */
    private ConcurrentSkipListMap<String, ActorName> registry =
            new ConcurrentSkipListMap<String, ActorName>();

    /**
     * Bind request classes.
     *
     * @throws Exception Any exceptions thrown while binding.
     */
    @Override
    public void bindery()
            throws Exception {
        super.bindery();

        thisActor.bind(
                RegisterActor.class.getName(),
                new VoidConcurrentMethodBinding<RegisterActor>() {
                    @Override
                    public void concurrentProcessRequest(RequestReceiver requestReceiver,
                                                         RegisterActor request)
                            throws Exception {
                        final ActorName actor = request.getActor();
                        String name = GetActorName.req.call(actor);
                        ActorName old = registry.putIfAbsent(name, actor);
                        if (old != null && old != actor)
                            throw new UnsupportedOperationException("Duplicate actor name.");
                    }
                });

        thisActor.bind(
                UnregisterActor.class.getName(),
                new VoidConcurrentMethodBinding<UnregisterActor>() {
                    @Override
                    public void concurrentProcessRequest(RequestReceiver requestReceiver,
                                                         UnregisterActor request)
                            throws Exception {
                        final String name = request.getName();
                        registry.remove(name);
                    }
                });

        thisActor.bind(
                GetRegisteredActor.class.getName(),
                new ConcurrentMethodBinding<GetRegisteredActor, Actor>() {
                    @Override
                    public Actor concurrentProcessRequest(RequestReceiver requestReceiver,
                                                          GetRegisteredActor request)
                            throws Exception {
                        String name = request.getName();
                        ActorName registeredActor = registry.get(name);
                        if (registeredActor == null && parentHasSameComponent()) {
                            Actor parent = requestReceiver.getParent();
                            return request.call(parent);
                        }
                        return (Actor) registeredActor;
                    }
                });
    }

    /*
     * Calls close on all the registered actors.
     *
     * @throws Exception All exceptions thrown will be ignored.
    @Override
    public void close() throws Exception {
        Iterator<ActorName> it = registry.values().iterator();
        while (it.hasNext()) {
            Actor actor = (Actor) it.next();
            actor.close();
        }
        super.close();
    }
     */
}
