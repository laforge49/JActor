package org.agilewiki.jactor.stateMachine;

import junit.framework.TestCase;
import org.agilewiki.jactor.*;
import org.agilewiki.jactor.lpc.JLPCActor;

public class OperationTest extends TestCase {
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
        protected void processRequest(Object unwrappedRequest, ResponseProcessor rp) throws Exception {
            SMBuilder smb = new SMBuilder();
            smb.add(new _Operation() {
                @Override
                public void call(final StateMachine sm, ResponseProcessor rp1) throws Exception {
                    JAIterator it = new JAIterator() {
                        int i;
                        int r = 1;
                        int max = ((Integer) sm.request).intValue();

                        @Override
                        protected void process(ResponseProcessor rp2) throws Exception {
                            if (i >= max) rp2.process(new Integer(r));
                            else {
                                i += 1;
                                r = r * i;
                                rp2.process(null);
                            }
                        }
                    };
                    it.iterate(rp1);
                }
            });
            smb.call(5, rp);
            //Output:
            //120
        }
    }
}
