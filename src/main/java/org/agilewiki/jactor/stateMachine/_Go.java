package org.agilewiki.jactor.stateMachine;

import org.agilewiki.jactor.ResponseProcessor;

public class _Go extends _Operation {
    private String label;

    public _Go(String label) {
        this.label = label;
    }

    @Override
    public void call(StateMachine stateMachine, ResponseProcessor rp) throws Exception {
        Integer loc = stateMachine.resolveLabel(label);
        if (loc == null) throw new IllegalArgumentException("unknown label: " + label);
        stateMachine.programCounter = loc.intValue();
        rp.process(null);
    }
}
