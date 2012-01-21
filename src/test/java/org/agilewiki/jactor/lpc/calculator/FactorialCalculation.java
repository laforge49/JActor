package org.agilewiki.jactor.lpc.calculator;

import org.agilewiki.jactor.JAIterator;
import org.agilewiki.jactor.Mailbox;
import org.agilewiki.jactor.ResponseProcessor;
import org.agilewiki.jactor.lpc.JLPCActor;
import org.agilewiki.jactor.stateMachine.ObjectFunc;
import org.agilewiki.jactor.stateMachine.StateMachine;

public class FactorialCalculation extends JLPCActor {
    public FactorialCalculation(Mailbox mailbox) {
        super(mailbox);
    }

    @Override
    protected void processRequest(final Object request, final ResponseProcessor rp) throws Exception {
        final Calculator calculator = new Calculator(getMailbox());
        send(calculator, new Set(1), new ResponseProcessor() {
            @Override
            public void process(Object response) throws Exception {
                JAIterator it = new JAIterator() {
                    Factorial factorial = (Factorial) request;
                    int max = factorial.getValue();
                    int count = 0;
                    @Override
                    protected void process(final ResponseProcessor rp1) throws Exception {
                        if (count == max) {
                            send(calculator, new Get(), rp1);
                            return;
                        }
                        count += 1;
                        send(calculator, new Multiply(count), new ResponseProcessor() {
                            @Override
                            public void process(Object response) throws Exception {
                                rp1.process(null);
                            }
                        });
                    }
                };
                it.iterate(rp);
            }
        });
    }
}
