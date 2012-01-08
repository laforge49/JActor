package org.agilewiki.jactor.bind;

import org.agilewiki.jactor.ResponseProcessor;
import org.agilewiki.jactor.apc.APCRequestSource;
import org.agilewiki.jactor.lpc.RequestSource;

/**
 * Binds to a request class.
 */
abstract public class Binding {
    public JBActor.Internals internals;

    /**
     * Process an incoming request.
     *
     * @param requestSource The originator of the request.
     * @param request          The request to be sent.
     * @param rp               The request processor.
     * @throws Exception Any uncaught exceptions raised while processing the request.
     */
    abstract public void acceptRequest(RequestSource requestSource,
                              Object request,
                              ResponseProcessor rp)
            throws Exception;
}
