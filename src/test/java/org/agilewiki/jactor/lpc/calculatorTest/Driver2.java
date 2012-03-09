package org.agilewiki.jactor.lpc.calculatorTest;

import org.agilewiki.jactor.Actor;
import org.agilewiki.jactor.Mailbox;
import org.agilewiki.jactor.RP;
import org.agilewiki.jactor.lpc.JLPCActor;

public class Driver2 extends JLPCActor {
    public Driver2(Mailbox mailbox) {
        super(mailbox);
    }

    @Override
    protected void processRequest(Object request, final RP rp)
            throws Exception {
        final Actor calculator = new Calculator(getMailbox());
        sendEvent(calculator, new Set(1));
        sendEvent(calculator, new Add(2));
        send(calculator, new Multiply(3), rp);
    }
}
