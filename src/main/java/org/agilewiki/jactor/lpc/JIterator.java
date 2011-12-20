package org.agilewiki.jactor.lpc;

import org.agilewiki.jactor.apc.Function;
import org.agilewiki.jactor.apc.ResponseProcessor;

/**
 * Iterates over a Function.
 */
final public class JIterator implements ResponseProcessor {
    private boolean sync = false;
    private boolean async = false;
    private Function function;
    private ResponseProcessor responseProcessor;

    JIterator(Function function, ResponseProcessor responseProcessor) {
        this.function = function;
        this.responseProcessor = responseProcessor;
    }

    @Override
    public void process(Object unwrappedResponse) throws Exception {
        if (unwrappedResponse == null) {
            if (!async) {
                sync = true;
            } else {
                JIterator it = new JIterator(function, responseProcessor);
                it.iterate(); //not recursive
            }
        } else responseProcessor.process(unwrappedResponse);
    }

    public void iterate() throws Exception {
        sync = true;
        while (sync) {
            sync = false;
            function.process(this);
            if (!sync) {
                async = true;
            }
        }
    }
}
