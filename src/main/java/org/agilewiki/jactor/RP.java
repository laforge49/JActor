package org.agilewiki.jactor;

/**
 * Types the response.
 */
abstract public class RP<RESPONSE_TYPE> {
    /**
     * Receives and processes a response.
     *
     * @param response The response.
     * @throws Exception Any uncaught exceptions raised when processing the response.
     */
    abstract public void processResponse(RESPONSE_TYPE response) throws Exception;

    /**
     * Returns true when no response is expected.
     *
     * @return True when no response is expected.
     */
    public boolean isEvent() {
        return false;
    }
}
