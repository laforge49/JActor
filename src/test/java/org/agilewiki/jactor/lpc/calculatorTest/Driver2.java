package org.agilewiki.jactor.lpc.calculatorTest;

import org.agilewiki.jactor.*;
import org.agilewiki.jactor.lpc.JLPCActor;

/**
 * Test code.
 */
public class Driver2 extends JLPCActor implements SimpleRequestReceiver {
    public Driver2(Mailbox mailbox) {
        super(mailbox);
    }

    @Override
    public void processRequest(SimpleRequest request, final RP rp)
            throws Exception {
        final Actor calculator = new Calculator(getMailbox());
        (new Set(1)).sendEvent(this, calculator);
        (new Add(2)).sendEvent(this, calculator);
        (new Multiply(3)).send(this, calculator, rp);
    }
}
