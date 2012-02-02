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
package org.agilewiki.jactor.components.pubsubComponent;

import org.agilewiki.jactor.*;
import org.agilewiki.jactor.bind.Internals;
import org.agilewiki.jactor.bind.RequestReceiver;
import org.agilewiki.jactor.bind.SyncBinding;
import org.agilewiki.jactor.components.Component;
import org.agilewiki.jactor.lpc.RequestSource;
import org.agilewiki.jactor.pubsub.*;

import java.util.Collections;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Implements publish/subscribe over Component.
 */
public class PubSubComponent extends Component {
    /**
     * The subscribing actors.
     */
    private Set<Actor> subscribers = Collections.newSetFromMap(new ConcurrentHashMap<Actor, Boolean>());

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

        internals.bind(Subscribe.class.getName(), new SyncBinding() {
            public void acceptRequest(RequestReceiver requestReceiver,
                                      RequestSource requestSource,
                                      Object request,
                                      ResponseProcessor rp)
                    throws Exception {
                Subscribe subscribe = (Subscribe) request;
                Actor subscriber = subscribe.getSubscriber();
                rp.process(subscribers.add(subscriber));
            }
        });

        internals.bind(Unsubscribe.class.getName(), new SyncBinding() {
            public void acceptRequest(RequestReceiver requestReceiver,
                                      RequestSource requestSource,
                                      Object request,
                                      ResponseProcessor rp)
                    throws Exception {
                Unsubscribe unsubscribe = (Unsubscribe) request;
                Actor subscriber = unsubscribe.getSubscriber();
                rp.process(subscribers.remove(subscriber));
            }
        });

        internals.bind(Publish.class.getName(), new SyncBinding() {
            public void acceptRequest(RequestReceiver requestReceiver,
                                      final RequestSource requestSource,
                                      final Object request,
                                      final ResponseProcessor rp)
                    throws Exception {
                final Iterator<Actor> sit = subscribers.iterator();
                JAIterator jaIterator = new JAIterator() {
                    Publish publish = (Publish) request;
                    Object broadcastRequest = publish.getRequest();
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
                        subscriber.acceptRequest(requestSource, broadcastRequest, psrp);
                        rp1.process(null);
                    }
                };
                jaIterator.iterate(JANoResponse.nrp);
            }
        });

        internals.bind(Subscribers.class.getName(), new SyncBinding() {
            public void acceptRequest(RequestReceiver requestReceiver,
                                      RequestSource requestSource,
                                      Object request,
                                      ResponseProcessor rp)
                    throws Exception {
                rp.process(subscribers);
            }
        });
    }
}
