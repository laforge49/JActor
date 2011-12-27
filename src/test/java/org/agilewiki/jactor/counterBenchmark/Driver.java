package org.agilewiki.jactor.counterBenchmark;

import org.agilewiki.jactor.JAIterator;
import org.agilewiki.jactor.Mailbox;
import org.agilewiki.jactor.ResponseProcessor;
import org.agilewiki.jactor.lpc.JLPCActor;

final public class Driver extends JLPCActor {
    private CounterActor counterActor;
    private long runs;

    public Driver(Mailbox mailbox, CounterActor counterActor, long runs) {
        super(mailbox);
        this.counterActor = counterActor;
        this.runs = runs;
    }

    @Override
    protected void processRequest(Object request,
                                  final ResponseProcessor rp)
            throws Exception {
        ResponseProcessor getAndReset = new ResponseProcessor() {
            @Override
            public void process(Object response) throws Exception {
                send(counterActor, new GetAndReset(), rp);
            }
        };
        (new JAIterator() {
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
        }).iterate(getAndReset);
    }
}
