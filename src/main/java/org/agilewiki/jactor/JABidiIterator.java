package org.agilewiki.jactor;

import org.agilewiki.jactor.simpleMachine.ExtendedResponseProcessor;

/**
 * <p>
 * Iterates over its process method and works within any actor which supports 2-way messages.
 * </p>
 *
 * TODO Write real doc with example!
 */
abstract public class JABidiIterator {
    /**
     * Iterates over the process method.
     *
     * @param finalResponseProcessor The response processor.
     * @throws Exception Any uncaught exceptions raised by the process method.
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void iterate(final RP finalResponseProcessor) throws Exception {
        ExtendedResponseProcessor erp = new ExtendedResponseProcessor() {
            @Override
            public void processResponse(Object otherResponse) throws Exception {
                final Object maybeFinalResponse = JABidiIterator.this.processResponse(otherResponse);
                if (maybeFinalResponse != null) {
                    // Done!
                    if (maybeFinalResponse instanceof JANull) {
                        finalResponseProcessor.processResponse(null);
                    } else {
                        finalResponseProcessor.processResponse(maybeFinalResponse);
                    }
                } else {
                    if (!async) {
                        sync = true;
                    } else {
                        iterate(finalResponseProcessor); //not recursive
                    }
                }
            }
        };
        erp.sync = true;
        while (erp.sync) {
            erp.sync = false;
            sendRequest(erp);
            if (!erp.sync) {
                erp.async = true;
            }
        }
    }

    /**
     * Performs the request to the other actor. Passes the given RP to it, to collect it's response.
     *
     * @param responseProcessor Processes the response of the other actor.
     * @throws Exception Any uncaught exceptions raised by the process method.
     */
    abstract protected void sendRequest(RP responseProcessor) throws Exception;

    /**
     * Processes the response of the other actor.
     * Should return non-null to indicate the final response to the initial caller.
     * It can also be JANull, to indicate null as a final response.
     *
     * @param response The last response of the other actor.
     * @throws Exception Any uncaught exceptions raised by the process method.
     */
    abstract protected Object processResponse(Object response) throws Exception;
}
