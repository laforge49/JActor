package org.agilewiki.jactor.stateMachine;

import junit.framework.TestCase;
import org.agilewiki.jactor.*;
import org.agilewiki.jactor.lpc.JLPCActor;

/**
 * Test code.
 */
public class ReturnVTest extends TestCase {
    public void test() {
        MailboxFactory mailboxFactory = JAMailboxFactory.newMailboxFactory(1);
        try {
            JAFuture future = new JAFuture();
            Actor actor1 = new ReturnV1(mailboxFactory.createMailbox());
            System.out.println(SimpleRequest.req.send(future, actor1));
            Actor actor2 = new ReturnV2(mailboxFactory.createMailbox());
            System.out.println(SimpleRequest.req.send(future, actor2));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mailboxFactory.close();
        }
    }

    class ReturnV1 extends JLPCActor implements SimpleRequestReceiver {

        ReturnV1(Mailbox mailbox) {
            super(mailbox);
        }

        @Override
        public void processRequest(SimpleRequest unwrappedRequest, RP rp) throws Exception {
            SMBuilder smb = new SMBuilder();
            String rv = "Hello world!";
            smb._return(rv);
            rv = null;
            smb.call(rp);
            //Output:
            //Hello world!
        }
    }

    class ReturnV2 extends JLPCActor implements SimpleRequestReceiver {

        ReturnV2(Mailbox mailbox) {
            super(mailbox);
        }

        @Override
        public void processRequest(SimpleRequest unwrappedRequest, RP rp) throws Exception {
            SMBuilder smb = new SMBuilder();
            smb._return(null);
            smb.call(rp);
            //Output:
            //null
        }
    }
}
