package org.agilewiki.jactor.stateMachine;

import org.agilewiki.jactor.ResponseProcessor;

final public class _Call extends _Operation {
    private String resultName;
    private _StateMachine comp;

    public _Call(_StateMachine comp, String resultName) {
        this.comp = comp;
        this.resultName = resultName;
    }

    @Override
    public void call(final _StateMachine stateMachine, final ResponseProcessor rp)
            throws Exception {
        final StateMachine oldStateMachine = stateMachine.getState();
        comp.call(new ResponseProcessor() {
            @Override
            final public void process(Object response) throws Exception {
                if (resultName != null) oldStateMachine.results.put(resultName, response);
                stateMachine.setState(oldStateMachine);
                rp.process(null);
            }
        });
    }
}
