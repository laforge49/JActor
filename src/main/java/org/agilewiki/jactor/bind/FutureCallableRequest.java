package org.agilewiki.jactor.bind;

import org.agilewiki.jactor.Actor;
import org.agilewiki.jactor.apc.APCRequestSource;

/**
 * Requests that can be called from JAFuture.
 */
public class FutureCallableRequest<RESPONSE_TYPE> extends ConstrainedRequest<RESPONSE_TYPE> {
    /**
     * Send a constrained request.
     * (Override this method for requests with known return types.)
     *
     * @param targetActor   The target actor.
     * @return The response.
     * @throws Exception Any uncaught exceptions raised while processing the request.
     */
    public RESPONSE_TYPE call(Actor targetActor)
            throws Exception {
        return (RESPONSE_TYPE) targetActor.acceptCall(null, this);
    }
}
