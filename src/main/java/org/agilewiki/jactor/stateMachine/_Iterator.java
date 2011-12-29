package org.agilewiki.jactor.stateMachine;

import org.agilewiki.jactor.JAIterator;
import org.agilewiki.jactor.ResponseProcessor;

public class _Iterator extends _Operation {
    JAIterator iterator;
    private String resultName;

    public _Iterator(JAIterator iterator, String resultName) {
        this.iterator = iterator;
        this.resultName = resultName;
    }

    @Override
    public void call(final StateMachine stateMachine, final ResponseProcessor rp) throws Exception {
        iterator.iterate(new ResponseProcessor() {
            @Override
            public void process(Object response) throws Exception {
                if (resultName != null) stateMachine.results.put(resultName, response);
                rp.process(null);
            }
        });
    }
}
