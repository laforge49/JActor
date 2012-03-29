/*
 * Copyright 2012 Bill La Forge
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
package org.agilewiki.jactor.lpc;

import org.agilewiki.jactor.*;
import org.agilewiki.jactor.apc.APCRequestSource;
import org.agilewiki.jactor.bind.Internals;
import org.agilewiki.jactor.bind.JBActor;

/**
 * A request.
 */
abstract public class Request<RESPONSE_TYPE, TARGET_TYPE> {
    /**
     * Returns true when targetActor is an instanceof TARGET_TYPE
     *
     * @param targetActor The actor to be called.
     * @return True when targetActor is an instanceof TARGET_TYPE.
     */
    abstract protected boolean isTargetType(Actor targetActor);

    /**
     * Searches the parent stack for the matching TARGET_TYPE.
     *
     * @param parent A stack of actors, or null.
     * @return The matching parent, or null.
     */
    final public Actor getTargetActor(Actor parent) {
        while (parent != null && !isTargetType(parent)) {
            parent = parent.getParent();
        }
        return parent;
    }

    /**
     * Send a request and waits for a response.
     *
     * @param future      The future.
     * @param targetActor The target actor.
     * @throws Exception Any uncaught exceptions raised while processing the request.
     */
    final public RESPONSE_TYPE send(JAFuture future, JBActor targetActor)
            throws Exception {
        return (RESPONSE_TYPE) future.send(targetActor, this);
    }

    /**
     * Send a request.
     *
     * @param senderInternals The sending actor's internals.
     * @param targetActor     The target actor.
     * @param rp              The response processor.
     * @throws Exception Any uncaught exceptions raised while processing the request.
     */
    final public void send(Internals senderInternals, JBActor targetActor, RP<RESPONSE_TYPE> rp)
            throws Exception {
        senderInternals.send(targetActor, this, rp);
    }

    /**
     * Send a request.
     *
     * @param requestSource The sender of the request.
     * @param targetActor   The target actor.
     * @param rp            The response processor.
     * @throws Exception Any uncaught exceptions raised while processing the request.
     */
    final public void send(APCRequestSource requestSource, JBActor targetActor, RP<RESPONSE_TYPE> rp)
            throws Exception {
        targetActor.acceptRequest(requestSource, this, rp);
    }


    /**
     * Send a request event.
     *
     * @param targetActor The target actor.
     * @throws Exception Any uncaught exceptions raised while processing the request.
     */
    final public void sendEvent(JBActor targetActor)
            throws Exception {
        targetActor.acceptRequest(JAEvent.requestSource, this, JANoResponse.nrp);
    }

    /**
     * Send a request and waits for a response.
     *
     * @param future      The future.
     * @param targetActor The target actor.
     * @throws Exception Any uncaught exceptions raised while processing the request.
     */
    final public RESPONSE_TYPE send(JAFuture future, Actor targetActor)
            throws Exception {
        if (targetActor instanceof JBActor)
            return (RESPONSE_TYPE) future.send(targetActor, this);
        if (isTargetType(targetActor))
            return (RESPONSE_TYPE) future.send(targetActor, this);
        Actor parent = targetActor.getParent();
        if (parent != null)
            return send(future, parent);
        throw new UnsupportedOperationException(
                "request: " + getClass().getName() +
                        " target actor: " + targetActor.getClass().getName() +
                        " target actor type: " + targetActor.getActorType());
    }

    /**
     * Send a request.
     *
     * @param senderInternals The sending actor's internals.
     * @param targetActor     The target actor.
     * @param rp              The response processor.
     * @throws Exception Any uncaught exceptions raised while processing the request.
     */
    final public void send(Internals senderInternals, Actor targetActor, RP<RESPONSE_TYPE> rp)
            throws Exception {
        if (targetActor instanceof JBActor) {
            senderInternals.send(targetActor, this, rp);
            return;
        }
        if (isTargetType(targetActor)) {
            senderInternals.send(targetActor, this, rp);
            return;
        }
        Actor parent = targetActor.getParent();
        if (parent != null) {
            send(senderInternals, parent, rp);
            return;
        }
        throw new UnsupportedOperationException(
                "request: " + getClass().getName() +
                        " target actor: " + targetActor.getClass().getName() +
                        " target actor type: " + targetActor.getActorType());
    }

    /**
     * Send a request.
     *
     * @param requestSource The sender of the request.
     * @param targetActor   The target actor.
     * @param rp            The response processor.
     * @throws Exception Any uncaught exceptions raised while processing the request.
     */
    final public void send(APCRequestSource requestSource, Actor targetActor, RP<RESPONSE_TYPE> rp)
            throws Exception {
        if (targetActor instanceof JBActor) {
            targetActor.acceptRequest(requestSource, this, rp);
            return;
        }
        if (isTargetType(targetActor)) {
            targetActor.acceptRequest(requestSource, this, rp);
            return;
        }
        Actor parent = targetActor.getParent();
        if (parent != null) {
            send(requestSource, parent, rp);
            return;
        }
        throw new UnsupportedOperationException(
                "request: " + getClass().getName() +
                        " target actor: " + targetActor.getClass().getName() +
                        " target actor type: " + targetActor.getActorType());
    }


    /**
     * Send a request event.
     *
     * @param targetActor The target actor.
     * @throws Exception Any uncaught exceptions raised while processing the request.
     */
    final public void sendEvent(Actor targetActor)
            throws Exception {
        if (targetActor instanceof JBActor) {
            targetActor.acceptRequest(JAEvent.requestSource, this, JANoResponse.nrp);
            return;
        }
        if (isTargetType(targetActor)) {
            targetActor.acceptRequest(JAEvent.requestSource, this, JANoResponse.nrp);
            return;
        }
        Actor parent = targetActor.getParent();
        if (parent != null) {
            sendEvent(parent);
            return;
        }
        throw new UnsupportedOperationException(
                "request: " + getClass().getName() +
                        " target actor: " + targetActor.getClass().getName() +
                        " target actor type: " + targetActor.getActorType());
    }

    /**
     * Send a request and waits for a response.
     *
     * @param future      The future.
     * @param targetActor The target actor.
     * @throws Exception Any uncaught exceptions raised while processing the request.
     */
    final public RESPONSE_TYPE send(JAFuture future, TARGET_TYPE targetActor)
            throws Exception {
        return (RESPONSE_TYPE) future.send((Actor) targetActor, this);
    }

    /**
     * Send a request.
     *
     * @param senderInternals The sending actor's internals.
     * @param targetActor     The target actor.
     * @param rp              The response processor.
     * @throws Exception Any uncaught exceptions raised while processing the request.
     */
    final public void send(Internals senderInternals, TARGET_TYPE targetActor, RP<RESPONSE_TYPE> rp)
            throws Exception {
        senderInternals.send((Actor) targetActor, this, rp);
    }

    /**
     * Send a request.
     *
     * @param requestSource The sender of the request.
     * @param targetActor   The target actor.
     * @param rp            The response processor.
     * @throws Exception Any uncaught exceptions raised while processing the request.
     */
    final public void send(APCRequestSource requestSource, TARGET_TYPE targetActor, RP<RESPONSE_TYPE> rp)
            throws Exception {
        ((Actor) targetActor).acceptRequest(requestSource, this, rp);
    }

    /**
     * Send a request event.
     *
     * @param targetActor The target actor.
     * @throws Exception Any uncaught exceptions raised while processing the request.
     */
    final public void sendEvent(TARGET_TYPE targetActor)
            throws Exception {
        ((Actor) targetActor).acceptRequest(JAEvent.requestSource, this, JANoResponse.nrp);
    }
}
