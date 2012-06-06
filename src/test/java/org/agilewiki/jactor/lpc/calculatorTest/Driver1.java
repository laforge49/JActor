package org.agilewiki.jactor.lpc.calculatorTest;

import org.agilewiki.jactor.RP;
import org.agilewiki.jactor.SimpleRequest;
import org.agilewiki.jactor.SimpleRequestReceiver;
import org.agilewiki.jactor.lpc.JLPCActor;

/**
 * Test code.
 */
public class Driver1 extends JLPCActor implements SimpleRequestReceiver {
    @Override
    public void processRequest(SimpleRequest request, final RP rp) throws Exception {
        final Calculator calculator = new Calculator();
        calculator.initialize(getMailbox());
        send(calculator, new Set(1), new RP() {
            @Override
            public void processResponse(Object response) throws Exception {
                (new Add(2)).send(Driver1.this, calculator, new RP() {
                    @Override
                    public void processResponse(Object response) throws Exception {
                        (new Multiply(3)).send(Driver1.this, calculator, rp);
                    }
                });
            }
        });
    }
}
