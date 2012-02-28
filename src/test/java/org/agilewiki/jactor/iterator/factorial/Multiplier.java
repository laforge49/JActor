package org.agilewiki.jactor.iterator.factorial;

import org.agilewiki.jactor.Mailbox;
import org.agilewiki.jactor.RP;
import org.agilewiki.jactor.lpc.JLPCActor;

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
