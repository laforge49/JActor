package org.agilewiki.jactor.counterBenchmark;

import org.agilewiki.jactor.JAIterator;
import org.agilewiki.jactor.Mailbox;
import org.agilewiki.jactor.ResponseProcessor;
import org.agilewiki.jactor.lpc.JLPCActor;

final public class Driver extends JLPCActor {
    Compose compose = new Compose();

    public Driver(Mailbox mailbox, final CounterActor counterActor, final long runs) {
        super(mailbox);
        compose._iterator(new JAIterator() {
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
        });
        compose._send(counterActor, new GetAndReset());
    }

    @Override
    protected void processRequest(Object request,
                                  final ResponseProcessor rp)
            throws Exception {
        compose.call(rp);
    }
}
