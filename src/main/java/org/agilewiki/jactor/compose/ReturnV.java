package org.agilewiki.jactor.compose;

import org.agilewiki.jactor.JANull;
import org.agilewiki.jactor.ResponseProcessor;

final public class ReturnV extends Operation{
    private Object value;

    public ReturnV(Object value) {
        this.value = value;
    }

    @Override
    final public void call(_Compose compose, ResponseProcessor rp) throws Exception {
        Object rv = value;
        if (rv == null) rv = new JANull();
        rp.process(rv);
    }
}
