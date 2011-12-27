package org.agilewiki.jactor.compose;

import org.agilewiki.jactor.JANull;
import org.agilewiki.jactor.ResponseProcessor;

final public class ReturnF extends Operation{
    private Func func;

    public ReturnF(Func func) {
        this.func = func;
    }

    @Override
    final public void call(_Compose compose, ResponseProcessor rp) throws Exception {
        Object rv = func.get();
        if (rv == null) rv = new JANull();
        rp.process(rv);
    }
}
