package org.agilewiki.jactor.stateMachine;

import org.agilewiki.jactor.ResponseProcessor;

final public class _Call extends _Operation {
    private String resultName;
    private _SMBuilder comp;

    public _Call(_SMBuilder comp, String resultName) {
        this.comp = comp;
        this.resultName = resultName;
    }

    @Override
    public void call(final StateMachine stateMachine, final ResponseProcessor rp)
            throws Exception {
        comp.call(new ResponseProcessor() {
            @Override
            final public void process(Object response) throws Exception {
                if (resultName != null) stateMachine.results.put(resultName, response);
                rp.process(null);
            }
        });
    }
}
