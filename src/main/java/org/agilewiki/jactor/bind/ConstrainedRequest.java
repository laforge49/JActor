package org.agilewiki.jactor.bind;

import org.agilewiki.jactor.Actor;
import org.agilewiki.jactor.apc.APCRequestSource;
import org.agilewiki.jactor.bind.Internals;

/**
 * A request that can be passed to an actor for processing via the Internals.call or Actor.acceptCall methods.
 */
public class ConstrainedRequest {
    /**
     * Send a constrained request.
     * (Override this method for requests with known return types.)
     *
     * @param sourceInternals The internals of the sending actor.
     * @param targetActor   The target actor.
     * @return The response.
     * @throws Exception Any uncaught exceptions raised while processing the request.
     */
    public Object call(Internals sourceInternals, Actor targetActor) throws Exception {
        return sourceInternals.call(targetActor, this);
    }

    /**
     * Send a constrained request.
     * (Override this method for requests with known return types.)
     *
     * @param apcRequestSource The sender of the request.
     * @param targetActor   The target actor.
     * @return The response.
     * @throws Exception Any uncaught exceptions raised while processing the request.
     */
    public Object acceptCall(APCRequestSource apcRequestSource, Actor targetActor)
            throws Exception {
        return targetActor.acceptCall(apcRequestSource, this);
    }
}
