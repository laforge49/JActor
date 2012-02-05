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
import org.agilewiki.jactor.bind.RequestReceiver;
import org.agilewiki.jactor.components.Component;
import org.agilewiki.jactor.components.Include;
import org.agilewiki.jactor.components.JCActor;
import org.agilewiki.jactor.components.actorName.SetActorName;
import org.agilewiki.jactor.components.actorRegistry.RegisterActor;
import org.agilewiki.jactor.lpc.RequestSource;

import java.util.concurrent.ConcurrentSkipListMap;

/**
 * <p>A component for defining actor types and creating instances,
 * where an actor type has a name and a component class.</p>
 */
public class Factory extends Component {
    /**
     * A table which maps type names to component classes.
     */
    private ConcurrentSkipListMap<String, Class> types = new ConcurrentSkipListMap<String, Class>();

    /**
     * Bind request classes.
     *
     * @throws Exception Any exceptions thrown while binding.
     */
    @Override
    public void bindery() throws Exception {
        super.bindery();

        thisActor.bind(DefineActorType.class.getName(), new ConcurrentMethodBinding<DefineActorType, Object>() {
            @Override
            public Object concurrentProcessRequest(RequestReceiver requestReceiver,
                                                   RequestSource requestSource,
                                                   DefineActorType defineActorType)
                    throws Exception {
                String actorType = defineActorType.getActorType();
                if (types.containsKey(actorType))
                    throw new IllegalArgumentException("Actor type is already defined: " + actorType);
                Class rootComponentClass = defineActorType.getRootComponentClass();
                types.put(actorType, rootComponentClass);
                return null;
            }
        });

        thisActor.bind(NewActor.class.getName(), new ConcurrentMethodBinding<NewActor, JCActor>() {
            @Override
            public JCActor concurrentProcessRequest(RequestReceiver requestReceiver,
                                                    RequestSource requestSource,
                                                    NewActor request)
                    throws Exception {
                String actorType = request.getActorType();
                Mailbox mailbox = request.getMailbox();
                Actor parent = request.getParent();
                if (mailbox == null || parent == null) {
                    if (mailbox == null) mailbox = requestReceiver.getMailbox();
                    if (parent == null) parent = requestReceiver.getThisActor();
                    request = new NewActor(actorType, mailbox, request.getActorName(), parent);
                }
                Class componentClass = types.get(actorType);
                if (componentClass == null) {
                    if (requestReceiver.parentHasSameComponent())
                        return request.acceptCall(requestSource, requestReceiver.getParent());
                    throw new IllegalArgumentException("Unknown actor type: " + actorType);
                }
                Include include = new Include(componentClass);
                String actorName = request.getActorName();
                JCActor actor = new JCActor(mailbox);
                actor.setActorType(actorType);
                actor.setParent(parent);
                actor.acceptCall(requestSource, include);
                if (actorName != null) {
                    actor.acceptCall(requestSource, new SetActorName(actorName));
                    thisActor.acceptCall(requestSource, new RegisterActor(actor));
                }
                return actor;
            }
        });
    }
}
