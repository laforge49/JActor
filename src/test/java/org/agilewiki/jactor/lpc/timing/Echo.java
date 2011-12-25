package org.agilewiki.jactor.lpc.timing;

import org.agilewiki.jactor.Mailbox;
import org.agilewiki.jactor.ResponseProcessor;
import org.agilewiki.jactor.lpc.JLPCActor;

final public class Echo extends JLPCActor {

    public Echo(Mailbox mailbox) {
        super(mailbox);
    }

    @Override
    protected void processRequest(Object unwrappedRequest, ResponseProcessor responseProcessor)
            throws Exception {
        responseProcessor.process(null);
    }
}
