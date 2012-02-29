package org.agilewiki.jactor.bind;

/**
 * Requests that can be called from outside an actor.
 */
public class ExternallyCallableRequest<RESPONSE_TYPE> extends ConstrainedRequest<RESPONSE_TYPE> {
    /**
     * Send a constrained request.
     * (Override this method for requests with known return types.)
     *
     * @param targetActor The target actor.
     * @return The response.
     * @throws Exception Any uncaught exceptions raised while processing the request.
     */
    public RESPONSE_TYPE call(JBActor targetActor)
            throws Exception {
        return (RESPONSE_TYPE) targetActor.acceptCall(null, this);
    }
}
