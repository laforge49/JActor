package org.agilewiki.jactor.stateMachine;

import junit.framework.TestCase;
import org.agilewiki.jactor.*;
import org.agilewiki.jactor.lpc.JLPCActor;

/**
 * Test code.
 */
public class IfFTest extends TestCase {
    public void test() {
        MailboxFactory mailboxFactory = JAMailboxFactory.newMailboxFactory(1);
        try {
            Actor actor = new IfF(mailboxFactory.createMailbox());
            JAFuture future = new JAFuture();
            System.out.println(future.send(actor, null));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mailboxFactory.close();
        }
    }

    class IfF extends JLPCActor {

        IfF(Mailbox mailbox) {
            super(mailbox);
        }

        @Override
        public void processRequest(Object unwrappedRequest, RP rp) throws Exception {
            SMBuilder smb = new SMBuilder();
            smb._if(new BooleanFunc() {
                @Override
                public boolean get(StateMachine sm) {
                    return true;
                }
            }, "skip");
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
