package org.agilewiki.jactor.stateMachine;

import org.agilewiki.jactor.JANull;
import org.agilewiki.jactor.ResponseProcessor;

abstract public class _Iterator implements _Operation {
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
        iterate(stateMachine, new ResponseProcessor() {
            @Override
            public void process(Object response) throws Exception {
                if (resultName != null) {
                    stateMachine.put(resultName, response);
                }
                rp.process(null);
            }
        });
    }

    abstract private class IRP implements ResponseProcessor {
        public boolean sync;
        public boolean async;
    }

    /**
     * Iterates over the process method.
     *
     * @param stateMachine The state machine driving the operation.
     * @throws Exception Any uncaught exceptions raised by the process method.
     */
    public void iterate(final StateMachine stateMachine, final ResponseProcessor responseProcessor) throws Exception {
        IRP irp = new IRP() {
            @Override
            public void process(Object response) throws Exception {
                if (response == null) {
                    if (!async) {
                        sync = true;
                    } else {
                        iterate(stateMachine, responseProcessor); //not recursive
                    }
                } else if (response instanceof JANull) responseProcessor.process(null);
                else responseProcessor.process(response);
            }
        };
        irp.sync = true;
        while (irp.sync) {
            irp.sync = false;
            process(stateMachine, irp);
            if (!irp.sync) {
                irp.async = true;
            }
        }
    }

    /**
     * Perform an iteration.
     *
     * @param stateMachine The state machine driving the operation.
     * @param responseProcessor Processes the response.
     * @throws Exception Any uncaught exceptions raised by the process method.
     */
    abstract protected void process(StateMachine stateMachine, ResponseProcessor responseProcessor) throws Exception;
}
