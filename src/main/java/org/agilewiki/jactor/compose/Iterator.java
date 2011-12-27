package org.agilewiki.jactor.compose;

import org.agilewiki.jactor.JAIterator;
import org.agilewiki.jactor.ResponseProcessor;

public class Iterator extends Operation {
    JAIterator iterator;
    private String resultName;

    public Iterator(JAIterator iterator, String resultName) {
        this.iterator = iterator;
        this.resultName = resultName;
    }

    @Override
    public void call(_Compose compose, ResponseProcessor rp) throws Exception {

    }
}
