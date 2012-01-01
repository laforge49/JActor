package org.agilewiki.jactor.stateMachine;

import org.agilewiki.jactor.JAIterator;
import org.agilewiki.jactor.JANull;
import org.agilewiki.jactor.ResponseProcessor;

abstract public class _Iterator extends JAIterator implements _Operation {
    /**
     * The name of the result, or null.
     */
    private String resultName;

    public _Iterator(_SMBuilder smb) {
        this(smb, null);
    }

    public _Iterator(_SMBuilder smb, String resultName) {
        this.resultName = resultName;
        smb.add(this);
    }

    /**
     * Perform the operation.
     *
     * @param stateMachine The state machine driving the operation.
     * @param rp           The response processor.
     * @throws Exception Any uncaught exceptions raised while performing the operation.
     */
    @Override
    public void call(final StateMachine stateMachine, final ResponseProcessor rp) throws Exception {
        init(stateMachine);
        iterate(new ResponseProcessor() {
            @Override
            public void process(Object response) throws Exception {
                if (resultName != null) {
                    stateMachine.put(resultName, response);
                }
                rp.process(null);
            }
        });
    }
    
    protected void init(StateMachine stateMachine) {}
}
