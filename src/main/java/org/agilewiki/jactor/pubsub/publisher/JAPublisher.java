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
package org.agilewiki.jactor.pubsub.publisher;

import org.agilewiki.jactor.Mailbox;
import org.agilewiki.jactor.RP;
import org.agilewiki.jactor.lpc.Request;
import org.agilewiki.jactor.pubsub.subscriber.JASubscriber;
import org.agilewiki.jactor.pubsub.subscriber.Subscriber;

import java.util.HashMap;
import java.util.Iterator;

/**
 * Implements Publisher.
 */
public class JAPublisher
        extends JASubscriber
        implements Publisher {
    /**
     * Table of subscribers, keyed by actor name.
     */
    protected HashMap<String, Subscriber> subscribers = new HashMap<String, Subscriber>();

    /**
     * Create a JAPublisher.
     *
     * @param mailbox A mailbox which may be shared with other actors.
     */
    public JAPublisher(Mailbox mailbox) {
        super(mailbox);
    }

    /**
     * Subscribe to the publisher.
     *
     * @param subscriber The subscribing actor.
     * @return True when a new subscriber has been added.
     */
    @Override
    public boolean subscribe(Subscriber subscriber)
            throws Exception {
        String actorName = subscriber.getActorName();
        if (subscribers.containsKey(actorName))
            return false;
        subscribers.put(actorName, subscriber);
        return true;
    }

    /**
     * Unsubscribe from the publisher.
     *
     * @param subscriberName The name of the subscribing actor.
     * @return True when an actor is unsubscribed.
     */
    @Override
    public boolean unsubscribe(String subscriberName)
            throws Exception {
        Subscriber subscriber = subscribers.remove(subscriberName);
        return subscriber != null;
    }

    /**
     * Get a subscriber.
     *
     * @param subscriberName The name of the subscriber.
     * @return The subscriber, or null.
     */
    @Override
    public Subscriber getSubscriber(String subscriberName)
            throws Exception {
        return subscribers.get(subscriberName);
    }

    /**
     * Publish a request to all the appropriate subscribers.
     *
     * @param publishRequest The request to be published.
     * @return The number of subscribers which received the request.
     */
    public int publish(Request publishRequest)
            throws Exception {
        int c = 0;
        Iterator<Subscriber> it = subscribers.values().iterator();
        while (it.hasNext()) {
            Subscriber subscriber = it.next();
            if (publishRequest.isTargetType(subscriber)) {
                c += 1;
                publishRequest.sendEvent(subscriber);
            }
        }
        return c;
    }

    /**
     * This actor's subscription has been dropped.
     *
     * @param publisher The publisher which has dropped the subscription.
     */
    @Override
    public void unsubscribed(Publisher publisher)
            throws Exception {
    }

    /**
     * The application method for processing requests sent to the actor.
     *
     * @param request A request.
     * @param rp      The response processor.
     * @throws Exception Any uncaught exceptions raised while processing the request.
     */
    @Override
    protected void processRequest(Object request, RP rp)
            throws Exception {
        Class reqcls = request.getClass();

        if (reqcls == Subscribe.class) {
            Subscribe subscribe = (Subscribe) request;
            rp.processResponse(subscribe(subscribe.subscriber));
            return;
        }

        if (reqcls == Unsubscribe.class) {
            Unsubscribe unsubscribe = (Unsubscribe) request;
            rp.processResponse(unsubscribe(unsubscribe.subscriberName));
            return;
        }

        if (reqcls == GetSubscriber.class) {
            GetSubscriber getSubscriber = (GetSubscriber) request;
            rp.processResponse(getSubscriber(getSubscriber.subscriberName));
            return;
        }

        if (reqcls == Publish.class) {
            Publish publish = (Publish) request;
            rp.processResponse(publish(publish.publishRequest));
            return;
        }

        super.processRequest(request, rp);
    }
}
