package org.agilewiki.jactor.synchronousProgrammingTest;

import org.agilewiki.jactor.Mailbox;
import org.agilewiki.jactor.RP;
import org.agilewiki.jactor.lpc.JLPCActor;

/**
 * Test code.
 */
public class Greeter extends JLPCActor {
    public Greeter(final Mailbox mailbox) {
        super(mailbox);
    }

    public String hi() {
        return "Hello world!";
    }

    @Override
    protected void processRequest(Object request, RP rp) throws Exception {
        if (request.getClass() == Hi.class) {
            rp.processResponse(hi());
            return;
        }
        throw new UnsupportedOperationException(request.getClass().getName());
    }
}
