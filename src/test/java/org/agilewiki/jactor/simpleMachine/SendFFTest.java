package org.agilewiki.jactor.simpleMachine;

import junit.framework.TestCase;
import org.agilewiki.jactor.*;
import org.agilewiki.jactor.lpc.JLPCActor;

/**
 * Test code.
 */
public class SendFFTest extends TestCase {
    public void test() {
        MailboxFactory mailboxFactory = JAMailboxFactory.newMailboxFactory(1);
        try {
            Send actor = new Send();
            actor.initialize(mailboxFactory.createMailbox());
            JAFuture future = new JAFuture();
            System.out.println(SimpleRequest.req.send(future, actor));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mailboxFactory.close();
        }
    }

    class Doubler extends JLPCActor implements IntegerReceiver {
        @Override
        public void processRequest(IntegerRequest request, RP rp)
                throws Exception {
            int req = request.value;
            rp.processResponse(req * 2);
        }
    }

    class Send extends JLPCActor implements SimpleRequestReceiver {
        @Override
        public void processRequest(SimpleRequest request, RP rp)
                throws Exception {
            SMBuilder smb = new SMBuilder();
            smb._send(new ActorFunc() {
                          @Override
                          public Actor get(SimpleMachine sm) throws Exception {
                              Doubler d = new Doubler();
                              d.initialize(getMailbox());
                              return d;
                          }
                      }, new ObjectFunc() {
                          @Override
                          public Object get(SimpleMachine sm) {
                              return new IntegerRequest(21);
                          }
                      }, "rsp"
            );
            smb._return(new ObjectFunc() {
                @Override
                public Object get(SimpleMachine sm) {
                    return sm.get("rsp");
                }
            });
            smb.call(rp);
            //Output:
            //42
        }
    }
}
