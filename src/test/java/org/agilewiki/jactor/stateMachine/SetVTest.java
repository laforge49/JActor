package org.agilewiki.jactor.stateMachine;

import junit.framework.TestCase;
import org.agilewiki.jactor.*;
import org.agilewiki.jactor.lpc.JLPCActor;

/**
 * Test code.
 */
public class SetVTest extends TestCase {
    public void test() {
        MailboxFactory mailboxFactory = JAMailboxFactory.newMailboxFactory(1);
        try {
            Actor actor = new SetV1(mailboxFactory.createMailbox());
            JAFuture future = new JAFuture();
            System.out.println(SimpleRequest.req.send(future, actor));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mailboxFactory.close();
        }
    }
}

class SetV1 extends JLPCActor implements SimpleRequestReceiver {

    SetV1(Mailbox mailbox) {
        super(mailbox);
    }

    @Override
    public void processRequest(SimpleRequest request, RP rp)
            throws Exception {
        SMBuilder smb = new SMBuilder();
        String sv = "Hello world!";
        smb._set(sv, "r1");
        sv = null;
        smb._set(new ObjectFunc() {
            @Override
            public Object get(StateMachine stateMachine) {
                System.out.println(stateMachine.get("r1"));
                return null;
            }
        });
        smb.call(rp);
        //Output:
        //Hello world!
        //null
    }
}
