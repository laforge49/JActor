package org.agilewiki.jactor.compose;

import org.agilewiki.jactor.ResponseProcessor;

public class _Go extends _Operation {
    private String label;

    public _Go(String label) {
        this.label = label;
    }

    @Override
    public void call(_Compose compose, ResponseProcessor rp) throws Exception {
        Integer loc = compose.labels.get(label);
        if (loc == null) throw new IllegalArgumentException("unknown label: " + label);
        compose.programCounter = loc.intValue();
        rp.process(null);
    }
}
