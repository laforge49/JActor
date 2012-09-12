package org.agilewiki.jactor.simpleMachine;

import junit.framework.TestCase;
import org.agilewiki.jactor.*;
import org.agilewiki.jactor.lpc.JLPCActor;

/**
 * Test code.
 */
public class IteratorTest extends TestCase {
    public void test() {
        MailboxFactory mailboxFactory = JAMailboxFactory.newMailboxFactory(1);
        try {
            It actor = new It();
            actor.initialize(mailboxFactory.createMailbox());
            JAFuture future = new JAFuture();
            System.out.println(SimpleRequest.req.send(future, actor));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mailboxFactory.close();
        }
    }

    class It extends JLPCActor implements SimpleRequestReceiver {
        @Override
        public void processRequest(SimpleRequest unwrappedRequest, RP rp) throws Exception {
            SMBuilder smb = new SMBuilder();
            new _Iterator(smb, "rs") {
                int i;
                int r = 1;
                int max;

                @Override
                protected void init(SimpleMachine sm) {
                    max = ((Integer) sm.request).intValue();
                }

                @Override
                protected void process(RP rp2) throws Exception {
                    if (i >= max) rp2.processResponse(new Integer(r));
                    else {
                        i += 1;
                        r = r * i;
                        rp2.processResponse(null);
                    }
                }
            };
            smb._return(new ObjectFunc() {
                @Override
                public Object get(SimpleMachine sm) {
                    return sm.get("rs");
                }
            });
            smb.call(5, rp);
            //Output:
            //120
        }
    }
}
