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
import org.agilewiki.jactor.bind.Binding;
import org.agilewiki.jactor.bind.JBActor;
import org.agilewiki.jactor.bind.MethodBinding;
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
                    public void processRequest(JBActor.Internals internals, Object request, final ResponseProcessor rp1)
                            throws Exception {
                        DefineActorType defineActorType = (DefineActorType) request;
                        String actorType = defineActorType.getActorType();
                        if (types.containsKey(actorType))
                            throw new IllegalArgumentException("Actor type is already defined: " + actorType);
                        Class rootComponentClass = defineActorType.getRootComponentClass();
                        types.put(actorType, rootComponentClass);
                        rp1.process(null);
                    }
                });

                bind(NewActor.class.getName(), new Binding() {
                    @Override
                    public void acceptRequest(RequestSource requestSource, Object request, ResponseProcessor rp)
                            throws Exception {
                        NewActor newActor = (NewActor) request;
                        String actorType = newActor.getActorType();
                        Mailbox mailbox = newActor.getMailbox();
                        Actor parent = newActor.getParent();
                        if (mailbox == null || parent == null) {
                            if (mailbox == null) mailbox = getMailbox();
                            if (parent == null) parent = getActor();
                            newActor = new NewActor(actorType, mailbox, newActor.getActorName(), parent);
                        }
                        if (types.containsKey(actorType)) {
                            internals.acceptRequest(requestSource, newActor, rp, this);
                            return;
                        }
                        if (parentHasSameComponent()) {
                            getParent().acceptRequest(requestSource, newActor, rp);
                            return;
                        }
                        throw new IllegalArgumentException("Unknown actor type: " + actorType);
                    }

                    @Override
                    public void processRequest(JBActor.Internals internals, Object request, final ResponseProcessor rp)
                            throws Exception {
                        NewActor newActor = (NewActor) request;
                        String actorType = newActor.getActorType();
                        Class componentClass = types.get(actorType);
                        Include include = new Include(componentClass);
                        Mailbox mailbox = newActor.getMailbox();
                        String actorName = newActor.getActorName();
                        Actor parent = newActor.getParent();
                        JCActor actor = new JCActor(mailbox);
                        actor.setActorType(actorType);
                        actor.setParent(parent);

                        SMBuilder smb = new SMBuilder();
                        smb._send(actor, include);
                        smb._if(actorName == null, "fin");
                        smb._send(actor, new SetActorName(actorName));
                        smb._send(getActor(), new RegisterActor(actor));
                        smb._label("fin");
                        smb._return(actor);
                        smb.call(rp);
                    }
                });

                rp.process(null);
            }
        });
    }
}
