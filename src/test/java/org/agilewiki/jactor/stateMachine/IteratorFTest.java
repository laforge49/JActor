package org.agilewiki.jactor.stateMachine;

import junit.framework.TestCase;
import org.agilewiki.jactor.*;
import org.agilewiki.jactor.lpc.JLPCActor;

public class IteratorFTest extends TestCase {
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
            smb._iterator(new IteratorFunc() {
                @Override
                public JAIterator get(final StateMachine sm) {
                    return new JAIterator() {
                        int i;
                        int r = 1;
                        int max = ((Integer) sm.request).intValue();

                        @Override
                        protected void process(ResponseProcessor rp) throws Exception {
                            if (i >= max) rp.process(new Integer(r));
                            else {
                                i += 1;
                                r = r * i;
                                rp.process(null);
                            }
                        }
                    };
                }
            }, "rs");
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
