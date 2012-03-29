package org.agilewiki.jactor.factory;

import org.agilewiki.jactor.Actor;
import org.agilewiki.jactor.lpc.ConcurrentRequest;

/**
 * Test code.
 */
final public class Hi extends ConcurrentRequest<Object, Foo> {

    /**
     * Send a synchronous request.
     *
     * @param targetActor The target actor.
     * @return The response.
     * @throws Exception Any uncaught exceptions raised while processing the request.
     */
    @Override
    public Object call(Foo targetActor)
            throws Exception {
        targetActor.hi();
        return null;
    }

    /**
     * Returns true when targetActor is an instanceof TARGET_TYPE
     *
     * @param targetActor The actor to be called.
     * @return True when targetActor is an instanceof TARGET_TYPE.
     */
    protected boolean isTargetType(Actor targetActor) {
        return targetActor instanceof Foo;
    }
}
