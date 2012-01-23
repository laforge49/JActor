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
package org.agilewiki.jactor.pubsub;

import org.agilewiki.jactor.*;
import org.agilewiki.jactor.lpc.JLPCActor;

import java.util.Collections;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Implements publish/subscribe over JLPCActor.
 */
public class PubSub extends JLPCActor {
    /**
     * The subscribing actors.
     */
    private Set<Actor> subscribers = Collections.newSetFromMap(new ConcurrentHashMap<Actor, Boolean>());

    /**
     * Create a PubSub actor.
     *
     * @param mailbox The mailbox.
     */
    public PubSub(Mailbox mailbox) {
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
    protected void processRequest(final Object request, final ResponseProcessor rp) throws Exception {
        if (request instanceof Publish) {
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
                    send(subscriber, broadcastRequest, psrp);
                    rp1.process(null);
                }
            };
            jaIterator.iterate(JANoResponse.nrp);
            return;
        }
        if (request instanceof Subscribe) {
            Subscribe subscribe = (Subscribe) request;
            Actor subscriber = subscribe.getSubscriber();
            rp.process(subscribers.add(subscriber));
            return;
        }
        if (request instanceof Unsubscribe) {
            Unsubscribe unsubscribe = (Unsubscribe) request;
            Actor subscriber = unsubscribe.getSubscriber();
            rp.process(subscribers.remove(subscriber));
            return;
        }
        if (request instanceof Subscribers) {
            rp.process(subscribers);
            return;
        }
        throw new UnsupportedOperationException(request.getClass().getName());
    }
}
