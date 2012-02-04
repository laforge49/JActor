package org.agilewiki.jactor.components.pubsub.timing;

import org.agilewiki.jactor.Mailbox;
import org.agilewiki.jactor.ResponseProcessor;
import org.agilewiki.jactor.lpc.JLPCActor;

final public class NullSubscriber extends JLPCActor {
    public NullSubscriber(Mailbox mailbox) {
        super(mailbox);
    }

    @Override
    protected void processRequest(Object request, ResponseProcessor rp) throws Exception {
        rp.process(null);
    }
}
