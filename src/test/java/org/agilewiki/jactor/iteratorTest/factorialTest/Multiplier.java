package org.agilewiki.jactor.iteratorTest.factorialTest;

import org.agilewiki.jactor.Mailbox;
import org.agilewiki.jactor.RP;
import org.agilewiki.jactor.lpc.JLPCActor;

/**
 * Test code.
 */
public class Multiplier extends JLPCActor {

    public Multiplier(Mailbox mailbox) {
        super(mailbox);
    }

    @Override
    public void processRequest(Object req, RP rp)
            throws Exception {
        Multiply m = (Multiply) req;
        rp.processResponse(new Integer(m.a * m.b));
    }
}
