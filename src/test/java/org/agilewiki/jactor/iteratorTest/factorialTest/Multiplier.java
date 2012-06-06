package org.agilewiki.jactor.iteratorTest.factorialTest;

import org.agilewiki.jactor.RP;
import org.agilewiki.jactor.lpc.JLPCActor;

/**
 * Test code.
 */
public class Multiplier extends JLPCActor {
    public void processRequest(Multiply m, RP rp)
            throws Exception {
        rp.processResponse(new Integer(m.a * m.b));
    }
}
