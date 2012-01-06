package org.agilewiki.jactor.iterator.factorial;

import org.agilewiki.jactor.Mailbox;
import org.agilewiki.jactor.ResponseProcessor;
import org.agilewiki.jactor.lpc.JLPCActor;

public class Multiplier extends JLPCActor {

    public Multiplier(Mailbox mailbox) {
        super(mailbox);
    }

    @Override
    public void processRequest(Object req, ResponseProcessor rp)
            throws Exception {
        Multiply m = (Multiply) req;
        rp.process(new Integer(m.a * m.b));
    }
}
