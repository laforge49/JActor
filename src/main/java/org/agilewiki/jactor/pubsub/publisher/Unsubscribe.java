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

import org.agilewiki.jactor.Actor;
import org.agilewiki.jactor.lpc.SynchronousRequest;

/**
 * Unsubscribe from a publisher.
 * The result returned is true when a subscriber has been removed.
 */
public class Unsubscribe extends SynchronousRequest<Boolean, Publisher> {
    /**
     * The name of the subscribing actor.
     */
    final public String subscriberName;

    /**
     * Create a Subscribe request.
     *
     * @param subscriberName The name of the subscribing actor.
     */
    public Unsubscribe(String subscriberName) {
        this.subscriberName = subscriberName;
    }

    /**
     * Send a synchronous request.
     *
     * @param targetActor The target actor.
     * @return The response.
     * @throws Boolean Any uncaught exceptions raised while processing the request.
     */
    @Override
    protected Boolean call(Publisher targetActor)
            throws Exception {
        return targetActor.unsubscribe(subscriberName);
    }

    /**
     * Returns true when targetActor is an instanceof TARGET_TYPE
     *
     * @param targetActor The actor to be called.
     * @return True when targetActor is an instanceof TARGET_TYPE.
     */
    protected boolean isTargetType(Actor targetActor) {
        return targetActor instanceof Publisher;
    }
}
