package org.agilewiki.jactor.stateMachine;

import junit.framework.TestCase;
import org.agilewiki.jactor.*;
import org.agilewiki.jactor.lpc.JLPCActor;

public class IteratorTest extends TestCase {
    public void test() {
        MailboxFactory mailboxFactory = JAMailboxFactory.newMailboxFactory(1);
        try {
            Actor actor = new It(mailboxFactory.createMailbox());
            JAFuture future = new JAFuture();
            System.out.println(future.send(actor, null));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mailboxFactory.close();
        }
    }

    class It extends JLPCActor {

        It(Mailbox mailbox) {
            super(mailbox);
        }

        @Override
        public void processRequest(Object unwrappedRequest, RP rp) throws Exception {
            SMBuilder smb = new SMBuilder();
            new _Iterator(smb, "rs") {
                int i;
                int r = 1;
                int max;

                @Override
                protected void init(StateMachine sm) {
                    max = ((Integer) sm.request).intValue();
                }

                @Override
                protected void process(RP rp2) throws Exception {
                    if (i >= max) rp2.process(new Integer(r));
                    else {
                        i += 1;
                        r = r * i;
                        rp2.process(null);
                    }
                }
            };
            smb._return(new ObjectFunc() {
                @Override
                public Object get(StateMachine sm) {
                    return sm.get("rs");
                }
            });
            smb.call(5, rp);
            //Output:
            //120
        }
    }
}
