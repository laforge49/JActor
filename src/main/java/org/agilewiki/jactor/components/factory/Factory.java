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
import org.agilewiki.jactor.ResponseProcessor;
import org.agilewiki.jactor.bind.JBActor;
import org.agilewiki.jactor.bind.MethodBinding;
import org.agilewiki.jactor.bind.SyncBinding;
import org.agilewiki.jactor.components.Component;
import org.agilewiki.jactor.components.JCActor;
import org.agilewiki.jactor.components.actorName.SetActorName;
import org.agilewiki.jactor.components.actorRegistry.RegisterActor;

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
     * Initialize the component after all its includes have been processed.
     * The response must always be null;
     *
     * @param internals The JBActor's internals.
     * @throws Exception Any exceptions thrown during the open.
     */
    @Override
    public void open(JBActor.Internals internals, final ResponseProcessor rp) throws Exception {
        super.open(internals, new ResponseProcessor() {
            @Override
            public void process(Object response) throws Exception {

                bind(DefineActorType.class.getName(), new MethodBinding() {
                    protected void processRequest(Object request, final ResponseProcessor rp1)
                            throws Exception {
                        DefineActorType defineActorType = (DefineActorType) request;
                        String actorType = defineActorType.getActorType();
                        if (types.containsKey(actorType))
                            throw new IllegalArgumentException("Actor type is already defined: "+actorType);
                        Class rootComponentClass = defineActorType.getRootComponentClass();
                        types.put(actorType, rootComponentClass);
                        rp1.process(null);
                    }
                });

                bind(NewActor.class.getName(), new SyncBinding() {
                    @Override
                    protected void processRequest(Object request, final ResponseProcessor rp1) 
                            throws Exception {
                        NewActor newActor = (NewActor) request;
                        String actorType = newActor.getActorType();
                        if (!types.containsKey(actorType)) {
                            if (parentHasSameComponent()) {
                                if (newActor.getParent() != null) {
                                    send(getParent(), request, rp1);
                                    return;
                                } else {
                                    Mailbox mailbox = newActor.getMailbox();
                                    if (mailbox == null) mailbox = getMailbox();
                                    NewActor newRequest =
                                            new NewActor(actorType, mailbox, newActor.getActorName(), getActor());
                                    send(getParent(), newRequest, rp1);
                                    return;
                                }
                            }
                            throw new IllegalArgumentException("Unknown actor type: "+actorType);
                        }
                        Mailbox mailbox = newActor.getMailbox();
                        if (mailbox == null) mailbox = getMailbox();
                        String actorName = newActor.getActorName();
                        Actor parent = newActor.getParent();
                        if (parent == null) parent = getActor();
                        final JCActor actor = new JCActor(mailbox);
                        actor.setParent(parent);
                        if (actorName == null) {
                            rp1.process(actor);
                            return;
                        }
                        SetActorName setActorName = new SetActorName(actorName);
                        send(actor, setActorName, new ResponseProcessor() {
                            @Override
                            public void process(Object response) throws Exception {
                                RegisterActor registerActor = new RegisterActor(actor);
                                send(getActor(), registerActor, new ResponseProcessor() {
                                    @Override
                                    public void process(Object response) throws Exception {
                                        rp1.process(actor);
                                    }
                                });
                            }
                        });
                    }
                });
                rp.process(null);
            }
        });
    }
}
