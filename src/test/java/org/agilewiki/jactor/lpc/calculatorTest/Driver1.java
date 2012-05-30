package org.agilewiki.jactor.lpc.calculatorTest;

import org.agilewiki.jactor.*;
import org.agilewiki.jactor.lpc.JLPCActor;

/**
 * Test code.
 */
public class Driver1 extends JLPCActor implements SimpleRequestReceiver {
    public Driver1(Mailbox mailbox) {
        super(mailbox);
    }

    @Override
    public void processRequest(SimpleRequest request, final RP rp) throws Exception {
        final Actor calculator = new Calculator(getMailbox());
        send(calculator, new Set(1), new RP() {
            @Override
            public void processResponse(Object response) throws Exception {
                send(calculator, new Add(2), new RP() {
                    @Override
                    public void processResponse(Object response) throws Exception {
                        send(calculator, new Multiply(3), rp);
                    }
                });
            }
        });
    }
}
