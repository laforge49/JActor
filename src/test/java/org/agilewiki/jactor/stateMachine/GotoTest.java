package org.agilewiki.jactor.stateMachine;

import junit.framework.TestCase;
import org.agilewiki.jactor.*;
import org.agilewiki.jactor.lpc.JLPCActor;

public class GotoTest extends TestCase {
    public void test() {
        MailboxFactory mailboxFactory = JAMailboxFactory.newMailboxFactory(1);
        try {
            Actor actor = new Goto(mailboxFactory.createMailbox());
            JAFuture future = new JAFuture();
            System.out.println(future.send(actor, null));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mailboxFactory.close();
        }
    }

    class Goto extends JLPCActor {

        Goto(Mailbox mailbox) {
            super(mailbox);
        }

        @Override
        protected void processRequest(Object unwrappedRequest, ResponseProcessor rp) throws Exception {
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
