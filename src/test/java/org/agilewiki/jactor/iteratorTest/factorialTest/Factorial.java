package org.agilewiki.jactor.iteratorTest.factorialTest;

import org.agilewiki.jactor.JAIterator;
import org.agilewiki.jactor.Mailbox;
import org.agilewiki.jactor.RP;
import org.agilewiki.jactor.lpc.JLPCActor;

/**
 * Test code.
 */
public class Factorial extends JLPCActor {
    public Factorial(Mailbox mailbox) {
        super(mailbox);
    }

    @Override
    public void processRequest(Object req, final RP rp)
            throws Exception {
        final int max = 5;
        RP printResult = new RP() {
            public void processResponse(Object rsp) throws Exception {
                System.out.println(rsp);
                rp.processResponse(null);
            }
        };
        (new JAIterator() {
            int i;
            int r = 1;
            Multiplier mp = new Multiplier(getMailbox());

            public void process(RP rp) throws Exception {
                if (i >= max) rp.processResponse(new Integer(r));
                else {
                    i += 1;
                    Multiply m = new Multiply();
                    m.a = r;
                    m.b = i;
                    send(mp, m, new RP() {
                        public void processResponse(Object rsp) throws Exception {
                            r = ((Integer) rsp).intValue();
                        }
                    });
                    rp.processResponse(null);
                }
            }
        }).iterate(printResult);
    }
}
