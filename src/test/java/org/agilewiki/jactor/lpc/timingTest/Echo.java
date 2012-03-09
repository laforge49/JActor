package org.agilewiki.jactor.lpc.timingTest;

import org.agilewiki.jactor.Mailbox;
import org.agilewiki.jactor.RP;
import org.agilewiki.jactor.lpc.JLPCActor;

/**
 * Test code.
 */
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
