package org.agilewiki.jactor.basics;

import org.agilewiki.jactor.RP;
import org.agilewiki.jactor.lpc.JLPCActor;

/**
 * Test code.
 */
public class Actor1 extends JLPCActor {
    protected void processRequest(Hi1 request, RP rp) throws Exception {
        rp.processResponse("Hello world!");
    }
}
