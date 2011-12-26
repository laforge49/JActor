package org.agilewiki.jactor.counterBenchmark;

import org.agilewiki.jactor.Mailbox;
import org.agilewiki.jactor.ResponseProcessor;
import org.agilewiki.jactor.lpc.JLPCActor;

final public class CounterActor extends JLPCActor {
    private long count = 0L;

    public CounterActor(Mailbox mailbox) {
        super(mailbox);
    }

    @Override
    protected void processRequest(Object request, 
                                  ResponseProcessor rp) 
            throws Exception {
        if (request instanceof AddCount) {
            AddCount addCount = (AddCount) request;
            count += addCount.number;
            rp.process(null);
        } else {
            Long current = new Long(count);
            count = 0;
            rp.process(current);
        }
    }
}
