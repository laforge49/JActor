package org.agilewiki.jactor.lpc;

import org.agilewiki.jactor.Actor;

/**
 * Requests that can be called from outside an actor.
 */
abstract public class ExternallyCallableRequest<RESPONSE_TYPE, TARGET_TYPE extends TargetActor>
        extends CallableRequest<RESPONSE_TYPE, TARGET_TYPE> {
    /**
     * Send an initialization request.
     *
     * @param targetActor The target actor.
     * @return The response.
     * @throws Exception Any uncaught exceptions raised while processing the request.
     */
    abstract public RESPONSE_TYPE call(TARGET_TYPE targetActor)
            throws Exception;

    /**
     * Send an initialization request.
     *
     * @param targetActor The target actor.
     * @return The response.
     * @throws Exception Any uncaught exceptions raised while processing the request.
     */
    final public RESPONSE_TYPE call(Actor targetActor)
            throws Exception {
        if (isTargetType(targetActor))
            return call((TARGET_TYPE) targetActor);
        Actor parent = targetActor.getParent();
        if (parent != null)
            return call(parent);
        throw new UnsupportedOperationException();
    }
}
