package org.agilewiki.jactor.apc.iterator.factorial;

import org.agilewiki.jactor.apc.ResponseProcessor;
import org.agilewiki.jactor.lpc.JLPCActor;
import org.agilewiki.jactor.lpc.Mailbox;

public class Multiplier extends JLPCActor {

    public Multiplier(Mailbox mailbox) {
        super(mailbox);
    }

    @Override
    protected void processRequest(Object req, ResponseProcessor rp)
            throws Exception {
        Multiply m = (Multiply) req;
        rp.process(new Integer(m.a * m.b));
    }
}
