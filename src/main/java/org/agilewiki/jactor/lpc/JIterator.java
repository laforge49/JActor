package org.agilewiki.jactor.lpc;

import org.agilewiki.jactor.apc.Function;
import org.agilewiki.jactor.apc.ResponseProcessor;

/**
 * Iterates over a Function.
 */
final public class JIterator implements ResponseProcessor {
    private boolean sync = false;
    private boolean async = false;
    private final Function function;
    private final ResponseProcessor responseProcessor;
    JIterator it = null;

    JIterator(Function function, ResponseProcessor responseProcessor) {
        this.function = function;
        this.responseProcessor = responseProcessor;
    }

    JIterator(Function function, ResponseProcessor responseProcessor, JIterator it) {
        this(function, responseProcessor);
        this.it = it;
    }

    @Override
    public void process(Object unwrappedResponse) throws Exception {
        if (unwrappedResponse == null) {
            if (!async) {
                sync = true;
            } else {
                if (it == null) it = new JIterator(function, responseProcessor, this);
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
