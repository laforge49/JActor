package org.agilewiki.jactor.lpc.timing;

import org.agilewiki.jactor.Mailbox;
import org.agilewiki.jactor.RP;
import org.agilewiki.jactor.lpc.JLPCActor;

final public class Echo extends JLPCActor {

    public Echo(Mailbox mailbox) {
        super(mailbox);
    }

    @Override
    public void processRequest(Object unwrappedRequest, RP responseProcessor)
            throws Exception {
        responseProcessor.processResponse(null);
    }
}
