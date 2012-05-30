package org.agilewiki.jactor.stateMachine;

import junit.framework.TestCase;
import org.agilewiki.jactor.*;
import org.agilewiki.jactor.lpc.JLPCActor;

/**
 * Test code.
 */
public class GotoTest extends TestCase {
    public void test() {
        MailboxFactory mailboxFactory = JAMailboxFactory.newMailboxFactory(1);
        try {
            Actor actor = new Goto(mailboxFactory.createMailbox());
            JAFuture future = new JAFuture();
            System.out.println(SimpleRequest.req.send(future, actor));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mailboxFactory.close();
        }
    }

    class Goto extends JLPCActor implements SimpleRequestReceiver {

        Goto(Mailbox mailbox) {
            super(mailbox);
        }

        @Override
        public void processRequest(SimpleRequest unwrappedRequest, RP rp) throws Exception {
            SMBuilder smb = new SMBuilder();
            smb._goto("skip");
            smb._set(new ObjectFunc() {
                @Override
                public Object get(StateMachine sm) {
                    System.out.println("does not print");
                    return null;
                }
            });
            smb._label("skip");
            smb._set(new ObjectFunc() {
                @Override
                public Object get(StateMachine stateMachine) {
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
