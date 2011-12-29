package org.agilewiki.jactor.stateMachine;

import org.agilewiki.jactor.ResponseProcessor;

public class _Go extends _Operation {
    private String label;

    public _Go(String label) {
        this.label = label;
    }

    @Override
    public void call(_SMBuilder smBuilder, ResponseProcessor rp) throws Exception {
        Integer loc = smBuilder.labels.get(label);
        if (loc == null) throw new IllegalArgumentException("unknown label: " + label);
        smBuilder.getState().programCounter = loc.intValue();
        rp.process(null);
    }
}
