package org.agilewiki.jactor.simpleMachine;

import junit.framework.TestCase;
import org.agilewiki.jactor.*;
import org.agilewiki.jactor.lpc.JLPCActor;

/**
 * Test code.
 */
public class SetFTest extends TestCase {
    public void test() {
        MailboxFactory mailboxFactory = JAMailboxFactory.newMailboxFactory(1);
        try {
            SetF1 actor = new SetF1();
            actor.initialize(mailboxFactory.createMailbox());
            JAFuture future = new JAFuture();
            System.out.println(SimpleRequest.req.send(future, actor));
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {
            mailboxFactory.close();
        }
    }

    class SetF1 extends JLPCActor implements SimpleRequestReceiver {
        @Override
        public void processRequest(SimpleRequest unwrappedRequest, RP rp) throws Exception {
            SMBuilder smb = new SMBuilder();
            smb._set(new ObjectFunc() {
                @Override
                public Object get(SimpleMachine stateMachine) {
                    System.out.println("Hello world!");
                    return null;
                }
            });
            smb._set(new ObjectFunc() {
                @Override
                public Object get(SimpleMachine stateMachine) {
                    return "42";
                }
            }, "r1");
            smb._return(new ObjectFunc() {
                @Override
                public Object get(SimpleMachine sm) {
                    return sm.get("r1");
                }
            });
            smb.call(rp);
            //Output:
            //Hello world!
            //42
        }
    }
}
