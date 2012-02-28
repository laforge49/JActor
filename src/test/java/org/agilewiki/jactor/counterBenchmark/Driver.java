package org.agilewiki.jactor.counterBenchmark;

import org.agilewiki.jactor.JAIterator;
import org.agilewiki.jactor.Mailbox;
import org.agilewiki.jactor.RP;
import org.agilewiki.jactor.lpc.JLPCActor;
import org.agilewiki.jactor.stateMachine.StateMachine;
import org.agilewiki.jactor.stateMachine._Operation;

final public class Driver extends JLPCActor {
    SMBuilder smb = new SMBuilder();

    public Driver(Mailbox mailbox, final CounterActor counterActor, final long runs) {
        super(mailbox);
        smb.add(new _Operation() {
            @Override
            public void call(final StateMachine sm, final RP rp1) throws Exception {
                JAIterator it = new JAIterator() {
                    long i = 0;

                    @Override
                    protected void process(RP rp1) throws Exception {
                        if (i == runs) rp1.processResponse(this);
                        else {
                            i += 1;
                            AddCount addCount = new AddCount();
                            addCount.number = 100L;
                            send(counterActor, addCount, rp1);
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
        smb._send(counterActor, new GetAndReset());
    }

    @Override
    public void processRequest(Object request,
                               final RP rp)
            throws Exception {
        smb.call(rp);
    }
}
