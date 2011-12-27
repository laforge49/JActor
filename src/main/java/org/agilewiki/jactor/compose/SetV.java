package org.agilewiki.jactor.compose;

import org.agilewiki.jactor.ResponseProcessor;

public class SetV extends Operation {
    private Object value;
    private String resultName;

    public SetV(Object value, String resultName) {
        this.value = value;
        this.resultName = resultName;
    }

    @Override
    public void call(_Compose compose, ResponseProcessor rp) throws Exception {
        if (resultName != null) compose.results.put(resultName, value);
        rp.process(null);
    }
}
