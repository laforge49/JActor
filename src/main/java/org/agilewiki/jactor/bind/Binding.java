package org.agilewiki.jactor.bind;

import org.agilewiki.jactor.ResponseProcessor;
import org.agilewiki.jactor.lpc.RequestSource;

/**
 * Binds a request class.
 */
abstract public class Binding {
    /**
     * Provides access to the JBActor internals.
     */
    public JBActor.Internals internals;

    /**
     * Process an incoming request.
     *
     * @param requestSource The originator of the request.
     * @param request       The request to be sent.
     * @param rp            The request processor.
     * @throws Exception Any uncaught exceptions raised while processing the request.
     */
    abstract public void acceptRequest(RequestSource requestSource,
                                       Object request,
                                       ResponseProcessor rp)
            throws Exception;

    /**
     * The application method for processing requests sent to the actor.
     *
     * @param request           A request.
     * @param rp The response processor.
     * @throws Exception Any uncaught exceptions raised while processing the request.
     */
    abstract protected void processRequest(Object request, ResponseProcessor rp)
            throws Exception;
}
