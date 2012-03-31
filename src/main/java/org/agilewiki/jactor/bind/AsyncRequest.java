package org.agilewiki.jactor.bind;

import org.agilewiki.jactor.Actor;
import org.agilewiki.jactor.lpc.Request;

/**
 * A request not handled by a JLPCActor subclass.
 */
public class AsyncRequest<RESPONSE_TYPE>
        extends Request<RESPONSE_TYPE, NoJLPCActor> {
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
