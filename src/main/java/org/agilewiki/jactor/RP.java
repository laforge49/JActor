package org.agilewiki.jactor;

/**
 * Types the response.
 */
abstract public class RP<RESPONSE_TYPE> extends ResponseProcessor {
    /**
     * Receives and processes a response.
     *
     * @param response The response.
     * @throws Exception Any uncaught exceptions raised when processing the response.
     */
    @Override
    final public void process(Object response) throws Exception {
        processResponse((RESPONSE_TYPE) response);
    }

    abstract public void processResponse(RESPONSE_TYPE response) throws Exception;
}
