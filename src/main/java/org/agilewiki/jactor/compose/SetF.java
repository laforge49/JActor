package org.agilewiki.jactor.compose;

import org.agilewiki.jactor.ResponseProcessor;

final public class SetF extends Operation {
    private Func func;
    private String resultName;

    public SetF(Func func, String resultName) {
        this.func = func;
        this.resultName = resultName;
    }

    @Override
    public void call(_Compose compose, ResponseProcessor rp) throws Exception {
        if (resultName != null) compose.results.put(resultName, func.get());
        rp.process(null);
    }
}
