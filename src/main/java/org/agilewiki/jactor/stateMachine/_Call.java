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
    public void call(final _SMBuilder smBuilder, final ResponseProcessor rp)
            throws Exception {
        final StateMachine oldStateMachine = smBuilder.getState();
        comp.call(new ResponseProcessor() {
            @Override
            final public void process(Object response) throws Exception {
                if (resultName != null) oldStateMachine.results.put(resultName, response);
                smBuilder.setState(oldStateMachine);
                rp.process(null);
            }
        });
    }
}
