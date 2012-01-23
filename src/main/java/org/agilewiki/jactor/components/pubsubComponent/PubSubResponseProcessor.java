package org.agilewiki.jactor.components.pubsubComponent;

import org.agilewiki.jactor.ResponseProcessor;

/**
 * Counts the number of requests sent and the number of responses received
 * and responds with the count of requests sent when the iteration is finished.
 */
public class PubSubResponseProcessor extends ResponseProcessor {
    /**
     * The number of requests sent.
     */
    public int sent;

    /**
     * The number of responses received.
     */
    public int received;

    /**
     * True when all responses have been received.
     */
    private boolean complete;

    /**
     * The mechanism for responding when finished.
     */
    private ResponseProcessor xrp;

    /**
     * Create a PubSubResponseProcessor.
     *
     * @param xrp The external response processor.
     */
    public PubSubResponseProcessor(ResponseProcessor xrp) {
        this.xrp = xrp;
    }

    /**
     * Signals that all requests have been sent.
     *
     * @throws Exception Any excpetions raised while processing the external response.
     */
    public void finished() throws Exception {
        if (received == sent)
            xrp.process(new Integer(sent));
        else
            complete = true;
    }

    /**
     * Receives and processes a response.
     *
     * @param response The response.
     * @throws Exception Any uncaught exceptions raised when processing the response.
     */
    @Override
    public void process(Object response) throws Exception {
        received += 1;
        if (complete && received == sent) {
            xrp.process(new Integer(sent));
        }
    }
}
