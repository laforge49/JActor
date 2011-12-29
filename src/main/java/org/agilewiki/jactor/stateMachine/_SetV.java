package org.agilewiki.jactor.stateMachine;

import org.agilewiki.jactor.ResponseProcessor;

public class _SetV extends _Operation {
    private Object value;
    private String resultName;

    public _SetV(Object value, String resultName) {
        this.value = value;
        this.resultName = resultName;
    }

    @Override
    public void call(_StateMachine stateMachine, ResponseProcessor rp) throws Exception {
        if (resultName != null) stateMachine.getState().results.put(resultName, value);
        rp.process(null);
    }
}
