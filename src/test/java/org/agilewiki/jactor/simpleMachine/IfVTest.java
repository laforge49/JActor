package org.agilewiki.jactor.simpleMachine;

import junit.framework.TestCase;
import org.agilewiki.jactor.*;
import org.agilewiki.jactor.lpc.JLPCActor;

/**
 * Test code.
 */
public class IfVTest extends TestCase {
    public void test() {
        MailboxFactory mailboxFactory = JAMailboxFactory.newMailboxFactory(1);
        try {
            IfV actor = new IfV();
            actor.initialize(mailboxFactory.createMailbox());
            JAFuture future = new JAFuture();
            System.out.println(SimpleRequest.req.send(future, actor));
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {
            mailboxFactory.close();
        }
    }

    class IfV extends JLPCActor implements SimpleRequestReceiver {
        @Override
        public void processRequest(SimpleRequest unwrappedRequest, RP rp) throws Exception {
            SMBuilder smb = new SMBuilder();
            boolean condition = true;
            smb._if(condition, "skip");
            condition = false;
            smb._set(new ObjectFunc() {
                @Override
                public Object get(SimpleMachine sm) {
                    System.out.println("does not print");
                    return null;
                }
            });
            smb._label("skip");
            smb._set(new ObjectFunc() {
                @Override
                public Object get(SimpleMachine stateMachine) {
                    System.out.println("Hello world!");
                    return null;
                }
            });
            smb.call(rp);
            //Output:
            //Hello world!
            //null
        }
    }
}
