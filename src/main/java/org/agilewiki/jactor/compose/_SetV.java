package org.agilewiki.jactor.compose;

import org.agilewiki.jactor.ResponseProcessor;

public class _SetV extends _Operation {
    private Object value;
    private String resultName;

    public _SetV(Object value, String resultName) {
        this.value = value;
        this.resultName = resultName;
    }

    @Override
    public void call(_Compose compose, ResponseProcessor rp) throws Exception {
        if (resultName != null) compose.getState().results.put(resultName, value);
        rp.process(null);
    }
}
