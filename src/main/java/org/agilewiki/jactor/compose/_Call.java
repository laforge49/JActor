package org.agilewiki.jactor.compose;

import org.agilewiki.jactor.ResponseProcessor;

final public class _Call extends _Operation {
    private String resultName;
    private _Compose comp;

    public _Call(_Compose comp, String resultName) {
        this.comp = comp;
        this.resultName = resultName;
    }

    @Override
    public void call(final _Compose compose, final ResponseProcessor rp)
            throws Exception {
        final State oldState = compose.getState();
        comp.call(new ResponseProcessor() {
            @Override
            final public void process(Object response) throws Exception {
                if (resultName != null) oldState.results.put(resultName, response);
                compose.setState(oldState);
                rp.process(null);
            }
        });
    }
}
