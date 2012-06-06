package org.agilewiki.jactor.lpc.exceptionsTest;

import org.agilewiki.jactor.RP;
import org.agilewiki.jactor.lpc.JLPCActor;

/**
 * Test code.
 */
public class Doer extends JLPCActor implements GoReceiver {
    @Override
    public void processRequest(Go1 request, RP rp) throws Exception {
        throw new Exception("Exception thrown in request processing");
    }

    @Override
    public void processRequest(Go2 request, RP rp) throws Exception {
        rp.processResponse(request);
    }
}
