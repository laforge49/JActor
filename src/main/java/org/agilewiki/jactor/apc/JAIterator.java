package org.agilewiki.jactor.apc;

/**
 * Iterates over its process method.
 */
abstract public class JAIterator {

    /**
     * Set to true when the response has not been received asynchronously.
     */
    private boolean sync = false;

    /**
     * Set to true when the response has not been received synchronously.
     */
    private boolean async = false;

    /**
     * @param responseProcessor Processes the final, non-null result.
     */
    private final ResponseProcessor responseProcessor;

    /**
     * The internal ResponseProcessor which handles the responses from the call to process.
     */
    private final ResponseProcessor irp = new ResponseProcessor() {
        @Override
        public void process(Object unwrappedResponse) throws Exception {
            if (unwrappedResponse == null) {
                if (!async) {
                    sync = true;
                } else {
                    iterate(); //not recursive
                }
            } else responseProcessor.process(unwrappedResponse);
        }
    };

    /**
     * Create a JAIterator.
     *
     * @param responseProcessor Processes the final, non-null result.
     * @throws Exception Any uncaught exceptions raised when calling the provided function.
     */
    public JAIterator(ResponseProcessor responseProcessor) throws Exception {
        this.responseProcessor = responseProcessor;
        iterate();
    }

    /**
     * Iterates over the process method.
     *
     * @throws Exception Any uncaught exceptions raised when calling the provided function.
     */
    private void iterate() throws Exception {
        sync = true;
        while (sync) {
            sync = false;
            process(irp);
            if (!sync) {
                async = true;
            }
        }
    }
    /**
     * Perform an iteration.
     *
     * @param responseProcessor Processes the response.
     * @throws Exception An exception raised when the function is called.
     */
    abstract protected void process(ResponseProcessor responseProcessor) throws Exception;
}
