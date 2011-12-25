package org.agilewiki.jactor.apc.iterator.factorial;

import org.agilewiki.jactor.apc.JAIterator;
import org.agilewiki.jactor.apc.ResponseProcessor;
import org.agilewiki.jactor.lpc.JLPCActor;
import org.agilewiki.jactor.lpc.Mailbox;

public class Factorial extends JLPCActor {

    public Factorial(Mailbox mailbox) {
        super(mailbox);
    }

    @Override
    protected void processRequest(Object req, final ResponseProcessor rp)
            throws Exception {
        final int max = 5;
        ResponseProcessor printResult = new ResponseProcessor() {
            public void process(Object rsp) throws Exception {
                System.out.println(rsp);
                rp.process(null);
            }
        };
        (new JAIterator(printResult) {
            int i;
            int r;
            Multiplier mp = new Multiplier(mailbox);

            public void process(ResponseProcessor rp) throws Exception {
                if (r == 0) r = 1;
                if (i >= max) rp.process(new Integer(r));
                else {
                    i += 1;
                    Multiply m = new Multiply();
                    m.a = r;
                    m.b = i;
                    send(mp, m, new ResponseProcessor() {
                        public void process(Object rsp) throws Exception {
                            r = ((Integer) rsp).intValue();
                        }
                    });
                    rp.process(null);
                }
            }
        }).iterate();
    }
}
