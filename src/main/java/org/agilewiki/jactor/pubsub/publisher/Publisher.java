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
import org.agilewiki.jactor.pubsub.subscriber.Subscriber;

/**
 * A publisher actor.
 */
public interface Publisher extends Subscriber {
    /**
     * Subscribe to the publisher.
     *
     * @param subscriber The subscribing actor.
     * @return True when a new name has been added.
     */
    public boolean subscribe(Subscriber subscriber)
            throws Exception;

    /**
     * Unsubscribe from the publisher.
     *
     * @param subscriber The subscribing actor.
     * @return True when an actor is unsubscribed.
     */
    public boolean unsubscribe(Subscriber subscriber)
            throws Exception;

    /**
     * Get a subscriber.
     *
     * @param subscriberName The name of the subscriber.
     * @return The subscriber, or null.
     */
    public Subscriber getSubscriber(String subscriberName)
            throws Exception;

    /**
     * Publish a request to all the appropriate subscribers.
     *
     * @param publishRequest The request to be published.
     * @param rp             The response processor.
     */
    public void publish(Request publishRequest, RP rp)
            throws Exception;
}
