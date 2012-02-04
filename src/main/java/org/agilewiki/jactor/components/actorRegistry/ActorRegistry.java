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
import org.agilewiki.jactor.ResponseProcessor;
import org.agilewiki.jactor.bind.ConcurrentMethodBinding;
import org.agilewiki.jactor.bind.Internals;
import org.agilewiki.jactor.bind.MethodBinding;
import org.agilewiki.jactor.bind.RequestReceiver;
import org.agilewiki.jactor.components.Component;
import org.agilewiki.jactor.components.JCActor;
import org.agilewiki.jactor.components.actorName.GetActorName;
import org.agilewiki.jactor.lpc.RequestSource;

import java.util.Iterator;
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
    private ConcurrentSkipListMap<String, JCActor> registry = new ConcurrentSkipListMap<String, JCActor>();

    /**
     * Initialize the component after all its includes have been processed.
     *
     * @param internals The JBActor's internals.
     * @throws Exception Any exceptions thrown during the open.
     */
    @Override
    public void open(final Internals internals)
            throws Exception {
        super.open(internals);

        internals.bind(RegisterActor.class.getName(), new MethodBinding() {
            public void processRequest(Internals internals, Object request, final ResponseProcessor rp1)
                    throws Exception {
                RegisterActor registerActor = (RegisterActor) request;
                final JCActor actor = registerActor.getActor();
                String name = (new GetActorName()).call(internals, actor);
                if (registry.containsKey(name))
                    throw new UnsupportedOperationException("Duplicate actor name.");
                registry.put(name, actor);
                rp1.process(null);
            }
        });

        internals.bind(UnregisterActor.class.getName(), new MethodBinding() {
            public void processRequest(Internals internals, Object request, final ResponseProcessor rp1)
                    throws Exception {
                UnregisterActor unregisterActor = (UnregisterActor) request;
                final String name = unregisterActor.getName();
                registry.remove(name);
                rp1.process(null);
            }
        });

        internals.bind(GetRegisteredActor.class.getName(), new ConcurrentMethodBinding<GetRegisteredActor>() {
            @Override
            public Object concurrentProcessRequest(RequestReceiver requestReceiver,
                                                   RequestSource requestSource,
                                                   GetRegisteredActor request)
                    throws Exception {
                String name = request.getName();
                JCActor registeredActor = registry.get(name);
                if (registeredActor == null && requestReceiver.parentHasSameComponent()) {
                    Actor parent = requestReceiver.getParent();
                    return parent.acceptCall(requestSource, request);
                }
                return registeredActor;
            }
        });
    }

    /**
     * Calls close on all the registered actors.
     *
     * @throws Exception All exceptions thrown will be ignored.
     */
    @Override
    public void close() throws Exception {
        Iterator<JCActor> it = registry.values().iterator();
        while (it.hasNext()) {
            JCActor actor = it.next();
            actor.close();
        }
        super.close();
    }
}
