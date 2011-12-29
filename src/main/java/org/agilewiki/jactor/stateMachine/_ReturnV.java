package org.agilewiki.jactor.stateMachine;

import org.agilewiki.jactor.JANull;
import org.agilewiki.jactor.ResponseProcessor;

final public class _ReturnV extends _Operation {
    private Object value;

    public _ReturnV(Object value) {
        this.value = value;
    }

    @Override
    final public void call(_StateMachine stateMachine, ResponseProcessor rp) throws Exception {
        Object rv = value;
        if (rv == null) rv = new JANull();
        rp.process(rv);
    }
}
