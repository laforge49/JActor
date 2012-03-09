package org.agilewiki.jactor.counterTest;

import org.agilewiki.jactor.Mailbox;
import org.agilewiki.jactor.RP;
import org.agilewiki.jactor.lpc.JLPCActor;

final public class CounterActor extends JLPCActor {
    private long count = 0L;

    public CounterActor(Mailbox mailbox) {
        super(mailbox);
    }

    @Override
    public void processRequest(Object request,
                               RP rp)
            throws Exception {
        if (request instanceof AddCount) {
            AddCount addCount = (AddCount) request;
            count += addCount.number;
            rp.processResponse(null);
        } else {
            Long current = new Long(count);
            count = 0;
            rp.processResponse(current);
        }
    }
}
