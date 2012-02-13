package org.agilewiki.jactor.stateMachine;

import junit.framework.TestCase;
import org.agilewiki.jactor.*;
import org.agilewiki.jactor.lpc.JLPCActor;

public class ReturnVTest extends TestCase {
    public void test() {
        MailboxFactory mailboxFactory = JAMailboxFactory.newMailboxFactory(1);
        try {
            JAFuture future = new JAFuture();
            Actor actor1 = new ReturnV1(mailboxFactory.createMailbox());
            System.out.println(future.send(actor1, null));
            Actor actor2 = new ReturnV2(mailboxFactory.createMailbox());
            System.out.println(future.send(actor2, null));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mailboxFactory.close();
        }
    }

    class ReturnV1 extends JLPCActor {

        ReturnV1(Mailbox mailbox) {
            super(mailbox);
        }

        @Override
        public void processRequest(Object unwrappedRequest, RP rp) throws Exception {
            SMBuilder smb = new SMBuilder();
            String rv = "Hello world!";
            smb._return(rv);
            rv = null;
            smb.call(rp);
            //Output:
            //Hello world!
        }
    }

    class ReturnV2 extends JLPCActor {

        ReturnV2(Mailbox mailbox) {
            super(mailbox);
        }

        @Override
        public void processRequest(Object unwrappedRequest, RP rp) throws Exception {
            SMBuilder smb = new SMBuilder();
            smb._return(null);
            smb.call(rp);
            //Output:
            //null
        }
    }
}
