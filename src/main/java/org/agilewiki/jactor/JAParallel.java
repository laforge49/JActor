package org.agilewiki.jactor;

import org.agilewiki.jactor.lpc.JLPCActor;

/**
 * Supports parallel request processing.
 */
final public class JAParallel extends JLPCActor {
    /**
     * The actors to be run in parallel.
     */
    private Actor[] actors;

    /**
     * Returns a response only when the expected number of responses are received.
     */
    private JAResponseCounter responseCounter;

    /**
     * Create a JAParallel actor.
     *
     * @param mailbox A mailbox which may be shared with other actors.
     * @param actors  The actors to be run in parallel.
     */
    public JAParallel(Mailbox mailbox, Actor[] actors) {
        super(mailbox);
        this.actors = actors;
        int p = actors.length;
    }

    /**
     * Sends either the same request to all the actors or a different request to each actor.
     *
     * @param request The request or an array of requests.
     * @param rd1     Process the response sent when responses from all the actors have been received.
     * @throws Exception Any uncaught exception thrown when the request is processed.
     */
    @Override
    protected void processRequest(final Object request, final ResponseProcessor rd1)
            throws Exception {
        int p = actors.length;
        responseCounter = new JAResponseCounter(p, rd1);
        int i = 0;
        if (request instanceof Object[]) {
            Object[] requests = (Object[]) request;
            if (requests.length != p)
                throw new IllegalArgumentException("Request and actor arrays not the same length");
            while (i < p) {
                send(actors[i], requests[i], responseCounter);
                i += 1;
            }
        } else while (i < p) {
            send(actors[i], request, responseCounter);
            i += 1;
        }
    }

    /**
     * Returns the response to be sent.
     *
     * @return The response to be sent.
     */
    public Object getResponse() {
        return responseCounter.getResponse();
    }

    /**
     * Assign the response to be sent.
     *
     * @param response The response to be sent.
     */
    public void setResponse(Object response) {
        responseCounter.setResponse(response);
    }
}
