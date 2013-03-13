package org.agilewiki.jactor.simpleMachine;

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
            ReturnF1 actor1 = new ReturnF1();
            actor1.initialize(mailboxFactory.createMailbox());
            System.out.println(SimpleRequest.req.send(future, actor1));
            ReturnF2 actor2 = new ReturnF2();
            actor2.initialize(mailboxFactory.createMailbox());
            System.out.println(SimpleRequest.req.send(future, actor2));
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {
            mailboxFactory.close();
        }
    }

    class ReturnF1 extends JLPCActor implements SimpleRequestReceiver {
        @Override
        public void processRequest(SimpleRequest unwrappedRequest, RP rp)
                throws Exception {
            SMBuilder smb = new SMBuilder();
            smb._return(new ObjectFunc() {
                @Override
                public Object get(SimpleMachine sm) {
                    return "Hello world!";
                }
            });
            smb.call(rp);
            //Output:
            //Hello world!
        }
    }

    class ReturnF2 extends JLPCActor implements SimpleRequestReceiver {
        @Override
        public void processRequest(SimpleRequest unwrappedRequest, RP rp) throws Exception {
            SMBuilder smb = new SMBuilder();
            smb._return(new ObjectFunc() {
                @Override
                public Object get(SimpleMachine sm) {
                    return null;
                }
            });
            smb.call(rp);
            //Output:
            //null
        }
    }
}
