package org.agilewiki.jactor.stateMachine;

import junit.framework.TestCase;
import org.agilewiki.jactor.*;
import org.agilewiki.jactor.lpc.JLPCActor;

/**
 * Test code.
 */
public class ReturnFTest extends TestCase {
    public void test() {
        MailboxFactory mailboxFactory = JAMailboxFactory.newMailboxFactory(1);
        try {
            JAFuture future = new JAFuture();
            Actor actor1 = new ReturnF1(mailboxFactory.createMailbox());
            System.out.println(future.send(actor1, null));
            Actor actor2 = new ReturnF2(mailboxFactory.createMailbox());
            System.out.println(future.send(actor2, null));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mailboxFactory.close();
        }
    }

    class ReturnF1 extends JLPCActor {

        ReturnF1(Mailbox mailbox) {
            super(mailbox);
        }

        @Override
        public void processRequest(Object unwrappedRequest, RP rp)
                throws Exception {
            SMBuilder smb = new SMBuilder();
            smb._return(new ObjectFunc() {
                @Override
                public Object get(StateMachine sm) {
                    return "Hello world!";
                }
            });
            smb.call(rp);
            //Output:
            //Hello world!
        }
    }

    class ReturnF2 extends JLPCActor {

        ReturnF2(Mailbox mailbox) {
            super(mailbox);
        }

        @Override
        public void processRequest(Object unwrappedRequest, RP rp) throws Exception {
            SMBuilder smb = new SMBuilder();
            smb._return(new ObjectFunc() {
                @Override
                public Object get(StateMachine sm) {
                    return null;
                }
            });
            smb.call(rp);
            //Output:
            //null
        }
    }
}
