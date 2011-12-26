package org.agilewiki.jactor;

final public class ResponseCounter implements ResponseProcessor {
    /**
     * Number of responses expected.
     */
    private int max;

    /**
     * Used to send the response on completion.
     */
    private ResponseProcessor rp;

    /**
     * The number of responses received.
     */
    private int c = 0;

    /**
     * The response to be sent on completion.
     */
    private Object response;

    /**
     * Create a ResponseCounter.
     *
     * @param max Number of responses expected.
     * @param rp  Used to send the response on completion.
     */
    public void responseCounter(int max, ResponseProcessor rp) {
        this.max = max;
        this.rp = rp;
    }

    /**
     * Receives and processes a response.
     *
     * @param response The response.
     * @throws Exception Any uncaught exceptions raised when processing the response.
     */
    @Override
    public void process(Object response) throws Exception {
        c += 1;
        if (c == max) rp.process(null);
    }

    /**
     * Returns the response to be sent.
     *
     * @return The response to be sent.
     */
    public Object getResponse() {
        return response;
    }

    /**
     * Assign the response to be sent.
     *
     * @param response The response to be sent.
     */
    public void setResponse(Object response) {
        this.response = response;
    }
}
