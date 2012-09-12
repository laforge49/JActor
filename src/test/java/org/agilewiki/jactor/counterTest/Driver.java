package org.agilewiki.jactor.counterTest;

import org.agilewiki.jactor.*;
import org.agilewiki.jactor.lpc.JLPCActor;
import org.agilewiki.jactor.simpleMachine.ObjectFunc;
import org.agilewiki.jactor.simpleMachine.SimpleMachine;
import org.agilewiki.jactor.simpleMachine._Operation;

/**
 * Test code.
 */
final public class Driver extends JLPCActor implements SimpleRequestReceiver {
    SMBuilder smb = new SMBuilder();

    public void initialize(Mailbox mailbox, final CounterActor counterActor, final long runs)
            throws Exception {
        super.initialize(mailbox);
        smb.add(new _Operation() {
            @Override
            public void call(final SimpleMachine sm, final RP rp1) throws Exception {
                JAIterator it = new JAIterator() {
                    long i = 0;

                    @Override
                    protected void process(RP rp1) throws Exception {
                        if (i == runs) rp1.processResponse(this);
                        else {
                            i += 1;
                            AddCount addCount = new AddCount();
                            addCount.number = 100L;
                            addCount.send(Driver.this, counterActor, rp1);
                        }
                    }
                };
                it.iterate(new RP() {
                    @Override
                    public void processResponse(Object response) throws Exception {
                        rp1.processResponse(null);
                    }
                });
            }
        });
        smb._send(counterActor, new GetAndReset(), "count");
        smb._return(new ObjectFunc() {
            @Override
            public Object get(SimpleMachine sm) {
                return sm.get("count");
            }
        });
    }

    @Override
    public void processRequest(SimpleRequest request,
                               final RP rp)
            throws Exception {
        smb.call(rp);
    }
}
