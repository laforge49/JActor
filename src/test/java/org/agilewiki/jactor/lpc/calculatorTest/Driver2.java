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
        sendEvent(calculator, new Set(1));
        sendEvent(calculator, new Add(2));
        send(calculator, new Multiply(3), rp);
    }
}
