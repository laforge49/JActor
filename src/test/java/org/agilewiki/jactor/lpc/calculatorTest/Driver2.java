package org.agilewiki.jactor.lpc.calculatorTest;

import org.agilewiki.jactor.RP;
import org.agilewiki.jactor.SimpleRequest;
import org.agilewiki.jactor.SimpleRequestReceiver;
import org.agilewiki.jactor.lpc.JLPCActor;

/**
 * Test code.
 */
public class Driver2 extends JLPCActor implements SimpleRequestReceiver {
    @Override
    public void processRequest(SimpleRequest request, final RP rp)
            throws Exception {
        final Calculator calculator = new Calculator();
        calculator.initialize(getMailbox());
        (new Set(1)).sendEvent(this, calculator);
        (new Add(2)).sendEvent(this, calculator);
        (new Multiply(3)).send(this, calculator, rp);
    }
}
