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

import org.agilewiki.jactor.RP;
import org.agilewiki.jactor.lpc.Request;
import org.agilewiki.jactor.parallel.JAResponseCounter3;
import org.agilewiki.jactor.pubsub.subscriber.JASubscriber;
import org.agilewiki.jactor.pubsub.subscriber.Subscriber;
import org.agilewiki.jactor.pubsub.subscriber.Unsubscribed;

import java.util.ArrayList;

/**
 * Implements Publisher.
 */
public class JAPublisher
        extends JASubscriber
        implements Publisher {
    /**
     * Table of subscribers, keyed by actor name.
     */
    protected ArrayList<Subscriber> subscribers = new ArrayList<Subscriber>();

    private ArrayList<JAResponseCounter3> pool = new ArrayList<JAResponseCounter3>();

    /**
     * Subscribe to the publisher.
     *
     * @param subscriber The subscribing actor.
     * @return True when a new name has been added.
     */
    @Override
    public boolean subscribe(Subscriber subscriber)
            throws Exception {
        String actorName = subscriber.getActorName();
        if (getSubscriber(actorName) != null)
            return false;
        subscribers.add(subscriber);
        return true;
    }

    /**
     * Unsubscribe from the publisher.
     *
     * @param subscriber The subscribing actor.
     * @return True when an actor is unsubscribed.
     */
    @Override
    public boolean unsubscribe(Subscriber subscriber)
            throws Exception {
        return subscribers.remove(subscriber);
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
        int i = 0;
        while (i < subscribers.size()) {
            Subscriber s = subscribers.get(i);
            if (s.getActorName().equals(subscriberName))
                return s;
            i += 1;
        }
        return null;
    }

    /**
     * Publish a request to all the appropriate subscribers.
     *
     * @param publishRequest The request to be published.
     * @param rp             The response processor.
     */
    public void publish(Request publishRequest, RP rp)
            throws Exception {
        JAResponseCounter3 rc;
        int ps = pool.size();
        if (ps == 0)
            rc = new JAResponseCounter3(pool);
        else {
            rc = pool.remove(ps - 1);
        }
        rc.setup(rp);
        int i = 0;
        while (i < subscribers.size()) {
            Subscriber s = subscribers.get(i);
            if (publishRequest.isTargetType(s)) {
                rc.sent += 1;
                publishRequest.send(this, s, rc);
            }
            i += 1;
        }
        rc.finished();

    }

    /**
     * This actor's subscription has been dropped.
     *
     * @param publisher The publisher which has dropped the subscription.
     * @param rp        The response processor.
     */
    @Override
    public void unsubscribed(Publisher publisher, RP rp)
            throws Exception {
        Unsubscribed unsubscribed = new Unsubscribed(this);
        publish(unsubscribed, rp);
    }
}
