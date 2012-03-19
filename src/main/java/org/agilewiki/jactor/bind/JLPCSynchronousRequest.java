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
package org.agilewiki.jactor.bind;

import org.agilewiki.jactor.*;
import org.agilewiki.jactor.apc.APCRequestSource;
import org.agilewiki.jactor.lpc.RequestSource;

/**
 * A request that can be passed to an Actor for synchronous processing,
 * but only when sender and receiver use the same mailbox.
 */
abstract public class JLPCSynchronousRequest<RESPONSE_TYPE, TARGET_TYPE>
        extends SynchronousRequest<RESPONSE_TYPE> {
    /**
     * Send a synchronous request.
     *
     * @param sourceInternals The internals of the sending actor.
     * @param targetActor     The target actor.
     * @return The response.
     * @throws Exception Any uncaught exceptions raised while processing the request.
     */
    final public RESPONSE_TYPE call(Internals sourceInternals, TARGET_TYPE targetActor)
            throws Exception {
        return call(sourceInternals.getThisActor().getMailbox(), targetActor);
    }

    /**
     * Send a synchronous request.
     *
     * @param requestSource The sender of the request.
     * @param targetActor   The target actor.
     * @return The response.
     * @throws Exception Any uncaught exceptions raised while processing the request.
     */
    final public RESPONSE_TYPE call(RequestSource requestSource, TARGET_TYPE targetActor)
            throws Exception {
        return call(requestSource.getMailbox(), targetActor);
    }

    /**
     * Send a synchronous request.
     *
     * @param srcActor    The sender of the request.
     * @param targetActor The target actor.
     * @return The response.
     * @throws Exception Any uncaught exceptions raised while processing the request.
     */
    final public RESPONSE_TYPE call(Actor srcActor, TARGET_TYPE targetActor)
            throws Exception {
        return call(srcActor.getMailbox(), targetActor);
    }

    /**
     * Send a synchronous request.
     *
     * @param sourceMailbox The sender of the request.
     * @param targetActor   The target actor.
     * @return The response.
     * @throws Exception Any uncaught exceptions raised while processing the request.
     */
    final protected RESPONSE_TYPE call(Mailbox sourceMailbox, TARGET_TYPE targetActor)
            throws Exception {
        if (sourceMailbox != ((Actor) targetActor).getMailbox())
            throw new UnsupportedOperationException("Mailboxes are not the same.");
        return call(targetActor);
    }

    /**
     * Send a synchronous request.
     *
     * @param targetActor The target actor.
     * @return The response.
     * @throws Exception Any uncaught exceptions raised while processing the request.
     */
    protected RESPONSE_TYPE call(TARGET_TYPE targetActor)
            throws Exception {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns true when targetActor is an instanceof TARGET_TYPE
     *
     * @param targetActor The actor to be called.
     * @return True when targetActor is an instanceof TARGET_TYPE.
     */
    abstract protected boolean isTargetType(Actor targetActor);

    /**
     * Send a synchronous request.
     *
     * @param sourceInternals The internals of the sending actor.
     * @param targetActor     The target actor.
     * @return The response.
     * @throws Exception Any uncaught exceptions raised while processing the request.
     */
    final public RESPONSE_TYPE call(Internals sourceInternals, Actor targetActor) throws Exception {
        if (targetActor instanceof JBActor)
            return call(sourceInternals, (JBActor) targetActor);
        if (isTargetType(targetActor))
            return call(sourceInternals, (TARGET_TYPE) targetActor);
        Actor parent = targetActor.getParent();
        if (parent != null)
            return call(sourceInternals, parent);
        throw new UnsupportedOperationException();
    }

    /**
     * Send a synchronous request.
     *
     * @param requestSource The sender of the request.
     * @param targetActor   The target actor.
     * @return The response.
     * @throws Exception Any uncaught exceptions raised while processing the request.
     */
    final public RESPONSE_TYPE call(RequestSource requestSource, Actor targetActor)
            throws Exception {
        if (targetActor instanceof JBActor)
            return call(requestSource, (JBActor) targetActor);
        if (isTargetType(targetActor))
            return call(requestSource, (TARGET_TYPE) targetActor);
        Actor parent = targetActor.getParent();
        if (parent != null)
            return call(requestSource, parent);
        throw new UnsupportedOperationException();
    }

    /**
     * Send a synchronous request.
     *
     * @param srcActor    The sender of the request.
     * @param targetActor The target actor.
     * @return The response.
     * @throws Exception Any uncaught exceptions raised while processing the request.
     */
    final public RESPONSE_TYPE call(Actor srcActor, Actor targetActor)
            throws Exception {
        if (targetActor instanceof JBActor)
            return call(srcActor, (JBActor) targetActor);
        if (isTargetType(targetActor))
            return call(srcActor, (TARGET_TYPE) targetActor);
        Actor parent = targetActor.getParent();
        if (parent != null)
            return call(srcActor, parent);
        throw new UnsupportedOperationException();
    }

    /**
     * Send a request and waits for a response.
     *
     * @param future      The future.
     * @param targetActor The target actor.
     * @throws Exception Any uncaught exceptions raised while processing the request.
     */
    @Override
    final public RESPONSE_TYPE send(JAFuture future, Actor targetActor)
            throws Exception {
        if (targetActor instanceof JBActor)
            return (RESPONSE_TYPE) future.send(targetActor, this);
        if (isTargetType(targetActor))
            return (RESPONSE_TYPE) future.send(targetActor, this);
        Actor parent = targetActor.getParent();
        if (parent != null)
            return send(future, parent);
        throw new UnsupportedOperationException();
    }

    /**
     * Send a request.
     *
     * @param senderInternals The sending actor's internals.
     * @param targetActor     The target actor.
     * @param rp              The response processor.
     * @throws Exception Any uncaught exceptions raised while processing the request.
     */
    @Override
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
        throw new UnsupportedOperationException();
    }

    /**
     * Send a request.
     *
     * @param requestSource The sender of the request.
     * @param targetActor   The target actor.
     * @param rp            The response processor.
     * @throws Exception Any uncaught exceptions raised while processing the request.
     */
    @Override
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
        throw new UnsupportedOperationException();
    }


    /**
     * Send a request event.
     *
     * @param targetActor The target actor.
     * @throws Exception Any uncaught exceptions raised while processing the request.
     */
    @Override
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
        throw new UnsupportedOperationException();
    }
}
