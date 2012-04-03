package org.agilewiki.jactor.bind;

import org.agilewiki.jactor.Actor;
import org.agilewiki.jactor.lpc.ConcurrentRequest;

/**
 * A concurrent request not handled by a JLPCActor subclass.
 */
public class JBConcurrentRequest<RESPONSE_TYPE>
        extends ConcurrentRequest<RESPONSE_TYPE, NoJLPCActor> {

    /**
     * Send a concurrent request.
     *
     * @param targetActor The target actor.
     * @return The response.
     * @throws Exception Any uncaught exceptions raised while processing the request.
     */
    public RESPONSE_TYPE call(NoJLPCActor targetActor)
            throws Exception {
        throw new Exception();
    }

    /**
     * Returns true when targetActor is an instanceof TARGET_TYPE
     *
     * @param targetActor The actor to be called.
     * @return True when targetActor is an instanceof TARGET_TYPE.
     */
    public boolean isTargetType(Actor targetActor) {
        return false;
    }
}
