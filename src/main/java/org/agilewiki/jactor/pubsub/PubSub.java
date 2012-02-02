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
import org.agilewiki.jactor.apc.APCRequestSource;

import java.util.Collections;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Implements publish/subscribe directly over Actor.
 * But PubSubComponent is faster.
 */
final public class PubSub implements Actor {
    /**
     * The subscribing actors.
     */
    private Set<Actor> subscribers = Collections.newSetFromMap(new ConcurrentHashMap<Actor, Boolean>());

    /**
     * Processes a purely synchronous method.
     * An exception will be thrown if the class of the request is not bound to a SyncMethodBinding.
     *
     * @param requestSource The originator of the request.
     * @param request       The request.
     * @return The response.
     * @throws Exception Any uncaught exceptions raised while processing the request.
     */
    @Override
    public Object acceptCall(final APCRequestSource requestSource, final Object request) throws Exception {
        if (request instanceof Publish) {
            final Iterator<Actor> sit = subscribers.iterator();
            while (sit.hasNext()) {
                Actor subscriber = sit.next();
                subscriber.acceptCall(requestSource, request);
            }
            return null;
        }
        if (request instanceof Subscribe) {
            Subscribe subscribe = (Subscribe) request;
            Actor subscriber = subscribe.getSubscriber();
            return subscribers.add(subscriber);
        }
        if (request instanceof Unsubscribe) {
            Unsubscribe unsubscribe = (Unsubscribe) request;
            Actor subscriber = unsubscribe.getSubscriber();
            return subscribers.remove(subscriber);
        }
        if (request instanceof Subscribers) {
            return subscribers;
        }
        throw new UnsupportedOperationException(request.getClass().getName());
    }

    /**
     * Wraps and enqueues an unwrapped request in the requester's inbox.
     *
     * @param requestSource The originator of the request.
     * @param request       The unwrapped request to be sent.
     * @param rp            The request processor.
     */
    @Override
    public void acceptRequest(final APCRequestSource requestSource, final Object request, final ResponseProcessor rp) throws Exception {
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
                    subscriber.acceptRequest(requestSource, broadcastRequest, psrp);
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

    /**
     * Set the initial capacity for buffered outgoing messages.
     *
     * @param initialBufferCapacity The initial capacity for buffered outgoing messages.
     */
    @Override
    public void setInitialBufferCapacity(int initialBufferCapacity) {
    }

    /**
     * Returns true when the concurrent data of the actor, or its parent, contains the named data item.
     *
     * @param name The key for the data item.
     * @return True when the concurrent data of the actor, or its parent, contains the named data item.
     */
    @Override
    public boolean hasDataItem(String name) {
        return false;
    }

    /**
     * Returns the actor's mailbox.
     *
     * @return The actor's mailbox.
     */
    @Override
    public Mailbox getMailbox() {
        throw new UnsupportedOperationException("PubSub has no mailbox");
    }
}
