package org.agilewiki.jactor.bind;

import org.agilewiki.jactor.ResponseProcessor;
import org.agilewiki.jactor.lpc.RequestSource;

/**
 * <p>
 * Binds a SynchronousRequest class to a purely synchronous method,
 * but restricts access to senders with the same mailbox.
 * </p>
 */
abstract public class SynchronousOnlyMethodBinding<REQUEST_TYPE, RESPONSE_TYPE>
        extends SynchronousMethodBinding<REQUEST_TYPE, RESPONSE_TYPE> {
    /**
     * <p>
     * Routes an incoming request by calling internals.routeRequest.
     * </p>
     *
     * @param requestReceiver The API used when a request is received.
     * @param requestSource   The originator of the request.
     * @param request         The request to be sent.
     * @param rp              The request processor.
     * @throws Exception Any uncaught exceptions raised while processing the request.
     */
    @Override
    public void acceptRequest(RequestReceiver requestReceiver,
                              RequestSource requestSource,
                              REQUEST_TYPE request,
                              ResponseProcessor rp)
            throws Exception {
        if (requestReceiver.getMailbox() != requestSource.getMailbox())
            throw new UnsupportedOperationException("mailboxes are not the same");
        requestReceiver.routeRequest(requestSource, request, rp, this);
    }
}
