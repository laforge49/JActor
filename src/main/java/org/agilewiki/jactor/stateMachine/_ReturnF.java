package org.agilewiki.jactor.stateMachine;

import org.agilewiki.jactor.JANull;
import org.agilewiki.jactor.ResponseProcessor;

final public class _ReturnF extends _Operation {
    private Func func;

    public _ReturnF(Func func) {
        this.func = func;
    }

    @Override
    final public void call(_SMBuilder smBuilder, ResponseProcessor rp) throws Exception {
        Object rv = func.get();
        if (rv == null) rv = new JANull();
        rp.process(rv);
    }
}
