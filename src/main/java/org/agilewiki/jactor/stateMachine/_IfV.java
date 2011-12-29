package org.agilewiki.jactor.stateMachine;

import org.agilewiki.jactor.ResponseProcessor;

final public class _IfV extends _Go {
    private boolean condition;

    public _IfV(boolean condition, String label) {
        super(label);
        this.condition = condition;
    }

    @Override
    public void call(_SMBuilder smBuilder, ResponseProcessor rp) throws Exception {
        if (condition) super.call(smBuilder, rp);
        else rp.process(null);
    }
}
