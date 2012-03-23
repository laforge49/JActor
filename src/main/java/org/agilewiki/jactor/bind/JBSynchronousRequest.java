package org.agilewiki.jactor.bind;

import org.agilewiki.jactor.Actor;
import org.agilewiki.jactor.lpc.SynchronousRequest;

/**
 * A synchronous request not handled by a JLPCActor subclass.
 */
public class JBSynchronousRequest<RESPONSE_TYPE>
        extends SynchronousRequest<RESPONSE_TYPE, NoJLPCActor> {

    /**
     * Send a synchronous request.
     *
     * @param targetActor The target actor.
     * @return The response.
     * @throws Exception Any uncaught exceptions raised while processing the request.
     */
    protected RESPONSE_TYPE call(NoJLPCActor targetActor)
            throws Exception {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns true when targetActor is an instanceof TARGET_TYPE
     *
     * @param targetActor The actor to be called.
     * @return True when targetActor is an instanceof TARGET_TYPE.
     */
    protected boolean isTargetType(Actor targetActor) {
        return false;
    }
}
