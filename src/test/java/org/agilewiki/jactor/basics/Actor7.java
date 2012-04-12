package org.agilewiki.jactor.basics;

import org.agilewiki.jactor.Mailbox;
import org.agilewiki.jactor.RP;
import org.agilewiki.jactor.lpc.JLPCActor;

/**
 * Test code.
 */
public class Actor7 extends JLPCActor {
    public long delay;

    public Actor7(Mailbox mailbox) {
        super(mailbox);
    }

    @Override
    protected void processRequest(Object request, final RP rp) throws Exception {
        Thread.sleep(delay);
        rp.processResponse(null);
    }
}
