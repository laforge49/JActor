package org.agilewiki.jactor.stateMachine;

import org.agilewiki.jactor.JAIterator;
import org.agilewiki.jactor.ResponseProcessor;

/**
 * <p>
 * A state machine compatible extension of JAIterator.
 * </p>
 *            SMBuilder smb = new SMBuilder();
 *            new _Iterator(smb, "rs") {
 *                int i;
 *                int r = 1;
 *                int max;
 *
 *                protected void init(StateMachine sm) {
 *                    max = ((Integer) sm.request).intValue();
 *                }
 *
 *                protected void process(ResponseProcessor rp2) throws Exception {
 *                    if (i >= max) rp2.process(new Integer(r));
 *                    else {
 *                        i += 1;
 *                        r = r * i;
 *                        rp2.process(null);
 *                    }
 *                }
 *            };
 *            smb._return(new ObjectFunc(){
 *                @Override
 *                public Object get(StateMachine sm) {
 *                    return sm.get("rs");
 *                }
 *            });
 *            smb.call(5, rp);
 *
 *            Response:
 *            120
*/
abstract public class _Iterator extends JAIterator implements _Operation {
    /**
     * The name of the result, or null.
     */
    private String resultName;

    /**
     * Create an _Iterator
     *
     * @param smb The state machine builder.
     */
    public _Iterator(_SMBuilder smb) {
        this(smb, null);
    }

    /**
     * Create an _Iterator
     *
     * @param smb The state machine builder.
     * @param resultName The name of the result, or null.
     */
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

    /**
     * Optional initialization, as required.
     *
     * @param stateMachine The state machine.
     */
    protected void init(StateMachine stateMachine) {}
}
