package org.agilewiki.jactor.counterBenchmark;

import org.agilewiki.jactor.JAIterator;
import org.agilewiki.jactor.Mailbox;
import org.agilewiki.jactor.ResponseProcessor;
import org.agilewiki.jactor.lpc.JLPCActor;
import org.agilewiki.jactor.stateMachine.StateMachine;
import org.agilewiki.jactor.stateMachine._Operation;

final public class Driver extends JLPCActor {
    SMBuilder smb = new SMBuilder();

    public Driver(Mailbox mailbox, final CounterActor counterActor, final long runs) {
        super(mailbox);
        smb.add(new _Operation() {
            @Override
            public void call(final StateMachine sm, final ResponseProcessor rp1) throws Exception {
                JAIterator it = new JAIterator() {
                    long i = 0;

                    @Override
                    protected void process(ResponseProcessor rp1) throws Exception {
                        if (i == runs) rp1.process(this);
                        else {
                            i += 1;
                            AddCount addCount = new AddCount();
                            addCount.number = 100L;
                            send(counterActor, addCount, rp1);
                        }
                    }
                };
                it.iterate(new ResponseProcessor() {
                    @Override
                    public void process(Object response) throws Exception {
                        rp1.process(null);
                    }
                });
            }
        });
        smb._send(counterActor, new GetAndReset());
    }

    @Override
    protected void processRequest(Object request,
                                  final ResponseProcessor rp)
            throws Exception {
        smb.call(rp);
    }
}
