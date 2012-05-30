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
            System.out.println(SimpleRequest.req.send(future, actor1));
            Actor actor2 = new ReturnF2(mailboxFactory.createMailbox());
            System.out.println(SimpleRequest.req.send(future, actor2));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mailboxFactory.close();
        }
    }

    class ReturnF1 extends JLPCActor implements SimpleRequestReceiver {

        ReturnF1(Mailbox mailbox) {
            super(mailbox);
        }

        @Override
        public void processRequest(SimpleRequest unwrappedRequest, RP rp)
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

    class ReturnF2 extends JLPCActor implements SimpleRequestReceiver {

        ReturnF2(Mailbox mailbox) {
            super(mailbox);
        }

        @Override
        public void processRequest(SimpleRequest unwrappedRequest, RP rp) throws Exception {
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
