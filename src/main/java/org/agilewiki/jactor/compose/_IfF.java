package org.agilewiki.jactor.compose;

import org.agilewiki.jactor.ResponseProcessor;

final public class _IfF extends _Go {
    private BooleanFunc condition;

    public _IfF(BooleanFunc condition, String label) {
        super(label);
        this.condition = condition;
    }

    @Override
    public void call(_Compose compose, ResponseProcessor rp) throws Exception {
        if (condition.get()) super.call(compose, rp);
        else rp.process(null);
    }
}
