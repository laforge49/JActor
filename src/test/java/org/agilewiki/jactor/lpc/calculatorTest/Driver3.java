package org.agilewiki.jactor.lpc.calculatorTest;

import org.agilewiki.jactor.*;
import org.agilewiki.jactor.lpc.JLPCActor;
import org.agilewiki.jactor.stateMachine.ObjectFunc;
import org.agilewiki.jactor.stateMachine.StateMachine;

/**
 * Test code.
 */
public class Driver3 extends JLPCActor implements SimpleRequestReceiver {
    public Driver3(Mailbox mailbox) {
        super(mailbox);
    }

    @Override
    public void processRequest(SimpleRequest request, final RP rp)
            throws Exception {
        final Actor calculator = new Calculator(getMailbox());
        final SMBuilder smb = new SMBuilder();
        smb._send(calculator, new Set(1));
        smb._send(calculator, new Add(2));
        smb._send(calculator, new Multiply(3), "result");
        smb._return(new ObjectFunc() {
            @Override
            public Object get(StateMachine sm) {
                return sm.get("result");
            }
        });
        smb.call(rp);
    }
}
