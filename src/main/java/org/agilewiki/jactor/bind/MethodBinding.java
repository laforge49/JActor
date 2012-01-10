package org.agilewiki.jactor.bind;

import org.agilewiki.jactor.ResponseProcessor;
import org.agilewiki.jactor.lpc.RequestSource;

/**
 * Binds a request class to a method.
 */
abstract public class MethodBinding extends Binding {
    /**
     * <p>
     * Routes an incoming request by calling internals.acceptRequest.
     * </p>
     *
     * @param requestSource The originator of the request.
     * @param request       The request to be sent.
     * @param rp            The request processor.
     * @throws Exception Any uncaught exceptions raised while processing the request.
     */
    @Override
    final public void acceptRequest(RequestSource requestSource, Object request, ResponseProcessor rp)
            throws Exception {
        internals.acceptRequest(requestSource, request, rp, this);
    }
}
