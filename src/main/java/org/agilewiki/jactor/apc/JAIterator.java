package org.agilewiki.jactor.apc;

/**
 * Iterates over a Function.
 */
final public class JAIterator implements ResponseProcessor {

    /**
     * Set to true when the response has not been received asynchronously.
     */
    private boolean sync = false;

    /**
     * Set to true when the response has not been received synchronously.
     */
    private boolean async = false;

    /**
     * @param function          Provides the function to be called.
     */
    private final Function function;

    /**
     * @param responseProcessor Processes the final, non-null result.
     */
    private final ResponseProcessor responseProcessor;

    /**
     * The other JAIterator being used.
     */
    private JAIterator it = null;

    /**
     * Create a JAIterator.
     *
     * @param function          Provides the function to be called.
     * @param responseProcessor Processes the final, non-null result.
     */
    private JAIterator(Function function, ResponseProcessor responseProcessor) {
        this.function = function;
        this.responseProcessor = responseProcessor;
    }

    /**
     * Create a JAIterator.
     *
     * @param function          Provides the function to be called.
     * @param responseProcessor Processes the final, non-null result.
     * @param it                The other JAIterator being used.
     */
    private JAIterator(Function function, ResponseProcessor responseProcessor, JAIterator it) {
        this(function, responseProcessor);
        this.it = it;
    }

    /**
     * Receives and processes a response.
     *
     * @param unwrappedResponse The response.
     * @throws Exception Any uncaught exceptions raised when processing the response.
     */
    @Override
    public void process(Object unwrappedResponse) throws Exception {
        if (unwrappedResponse == null) {
            if (!async) {
                sync = true;
            } else {
                if (it == null) it = new JAIterator(function, responseProcessor, this);
                it.iterate(); //not recursive
            }
        } else responseProcessor.process(unwrappedResponse);
    }

    /**
     * Iterates over a Function.
     *
     * @throws Exception Any uncaught exceptions raised when calling the provided function.
     */
    private void iterate() throws Exception {
        sync = true;
        while (sync) {
            sync = false;
            function.process(this);
            if (!sync) {
                async = true;
            }
        }
    }

    /**
     * Iterates over a Function.
     *
     * @param function          Provides the function to be called.
     * @param responseProcessor Processes the final, non-null result.
     * @throws Exception Any uncaught exceptions raised when calling the provided function.
     */
    public static void iterate(Function function, ResponseProcessor responseProcessor) throws Exception {
        JAIterator it = new JAIterator(function, responseProcessor);
        it.iterate();
    }
}
