package org.agilewiki.jactor.bind;

import org.agilewiki.jactor.ResponseProcessor;

/**
 * <p>
 * Binds a SynchronousRequest class to a purely synchronous method.
 * </p>
 */
abstract public class SynchronousMethodBinding extends MethodBinding {
    /**
     * A safe method for processing requests sent to the actor.
     *
     * @param internals The internal API of the receiving actor.
     * @param request   A request.
     * @param rp        The response processor.
     * @throws Exception Any uncaught exceptions raised while processing the request.
     */
    @Override
    final public void processRequest(Internals internals, Object request, ResponseProcessor rp) throws Exception {
        rp.process(synchronousProcessRequest(internals, (SynchronousRequest) request));
    }

    abstract public Object synchronousProcessRequest(Internals internals, SynchronousRequest request);
}
