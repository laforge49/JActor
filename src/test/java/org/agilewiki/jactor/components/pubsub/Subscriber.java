package org.agilewiki.jactor.components.pubsub;

import org.agilewiki.jactor.Mailbox;
import org.agilewiki.jactor.RP;
import org.agilewiki.jactor.lpc.JLPCActor;

public class Subscriber extends JLPCActor {
    public Subscriber(Mailbox mailbox) {
        super(mailbox);
    }

    @Override
    protected void processRequest(Object request, RP rp) throws Exception {
        System.err.println("Got request.");
        rp.process(null);
    }
}
