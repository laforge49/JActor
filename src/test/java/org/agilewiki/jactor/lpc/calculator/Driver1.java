package org.agilewiki.jactor.lpc.calculator;

import org.agilewiki.jactor.Actor;
import org.agilewiki.jactor.Mailbox;
import org.agilewiki.jactor.ResponseProcessor;
import org.agilewiki.jactor.lpc.JLPCActor;

public class Driver1 extends JLPCActor {
    public Driver1(Mailbox mailbox) {
        super(mailbox);
    }

    @Override
    protected void processRequest(Object request, final ResponseProcessor rp) throws Exception {
        final Actor calculator = new Calculator(getMailbox());
        send(calculator, new Set(1), new ResponseProcessor() {
            @Override
            public void process(Object response) throws Exception {
                send(calculator, new Add(2), new ResponseProcessor() {
                    @Override
                    public void process(Object response) throws Exception {
                        send(calculator, new Multiply(3), rp);
                    }
                });
            }
        });
    }
}
