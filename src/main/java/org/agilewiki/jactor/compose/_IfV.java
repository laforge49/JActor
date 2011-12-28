package org.agilewiki.jactor.compose;

import org.agilewiki.jactor.ResponseProcessor;

final public class _IfV extends _Go {
    private boolean condition;

    public _IfV(boolean condition, String label) {
        super(label);
        this.condition = condition;
    }

    @Override
    public void call(_Compose compose, ResponseProcessor rp) throws Exception {
        if (condition) super.call(compose, rp);
        else rp.process(null);
    }
}
