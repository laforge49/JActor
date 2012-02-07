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
package org.agilewiki.jactor.components.pubsub;

import org.agilewiki.jactor.*;
import org.agilewiki.jactor.bind.ConcurrentBinding;
import org.agilewiki.jactor.bind.ConcurrentMethodBinding;
import org.agilewiki.jactor.bind.Request;
import org.agilewiki.jactor.bind.RequestReceiver;
import org.agilewiki.jactor.components.Component;
import org.agilewiki.jactor.lpc.RequestSource;

import java.util.Collections;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Implements publish/subscribe over Component.
 */
public class PubSub extends Component {
    /**
     * The subscribing actors.
     */
    private Set<Actor> subscribers = Collections.newSetFromMap(new ConcurrentHashMap<Actor, Boolean>());

    /**
     * Bind request classes.
     *
     * @throws Exception Any exceptions thrown while binding.
     */
    @Override
    public void bindery()
            throws Exception {
        super.bindery();

        thisActor.bind(Subscribe.class.getName(), new ConcurrentMethodBinding<Subscribe, Boolean>() {
            @Override
            public Boolean concurrentProcessRequest(RequestReceiver requestReceiver,
                                                    Subscribe request)
                    throws Exception {
                Actor subscriber = request.getSubscriber();
                return subscribers.add(subscriber);
            }
        });

        thisActor.bind(Unsubscribe.class.getName(), new ConcurrentMethodBinding<Unsubscribe, Boolean>() {
            @Override
            public Boolean concurrentProcessRequest(RequestReceiver requestReceiver, Unsubscribe request) throws Exception {
                Actor subscriber = request.getSubscriber();
                return subscribers.remove(subscriber);
            }
        });

        thisActor.bind(Publish.class.getName(), new ConcurrentBinding<Publish>() {
            public void acceptRequest(RequestReceiver requestReceiver,
                                      final RequestSource requestSource,
                                      final Publish request,
                                      final ResponseProcessor rp)
                    throws Exception {
                final Iterator<Actor> sit = subscribers.iterator();
                JAIterator jaIterator = new JAIterator() {
                    Request broadcastRequest = request.getRequest();
                    final PubSubResponseProcessor psrp = new PubSubResponseProcessor(rp);

                    @Override
                    protected void process(ResponseProcessor rp1) throws Exception {
                        if (!sit.hasNext()) {
                            psrp.finished();
                            rp1.process(JANull.jan);
                            return;
                        }
                        Actor subscriber = sit.next();
                        psrp.sent += 1;
                        broadcastRequest.send(requestSource, subscriber, psrp);
                        rp1.process(null);
                    }
                };
                jaIterator.iterate(JANoResponse.nrp);
            }
        });

        thisActor.bind(Subscribers.class.getName(), new ConcurrentMethodBinding<Subscribers, Set<Actor>>() {
            @Override
            public Set<Actor> concurrentProcessRequest(RequestReceiver requestReceiver,
                                                       Subscribers request)
                    throws Exception {
                return subscribers;
            }
        });
    }
}
