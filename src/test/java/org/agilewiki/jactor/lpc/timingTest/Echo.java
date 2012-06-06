package org.agilewiki.jactor.lpc.timingTest;

import org.agilewiki.jactor.RP;
import org.agilewiki.jactor.SimpleRequest;
import org.agilewiki.jactor.SimpleRequestReceiver;
import org.agilewiki.jactor.lpc.JLPCActor;

/**
 * Test code.
 */
final public class Echo extends JLPCActor implements SimpleRequestReceiver {
    @Override
    public void processRequest(SimpleRequest unwrappedRequest, RP responseProcessor)
            throws Exception {
        responseProcessor.processResponse(null);
    }
}
