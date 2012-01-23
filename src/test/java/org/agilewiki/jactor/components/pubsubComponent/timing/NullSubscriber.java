package org.agilewiki.jactor.components.pubsubComponent.timing;

import org.agilewiki.jactor.Mailbox;
import org.agilewiki.jactor.ResponseProcessor;
import org.agilewiki.jactor.lpc.JLPCActor;

public class NullSubscriber extends JLPCActor {
    public NullSubscriber(Mailbox mailbox) {
        super(mailbox);
    }

    @Override
    protected void processRequest(Object request, ResponseProcessor rp) throws Exception {
        rp.process(null);
    }
}
