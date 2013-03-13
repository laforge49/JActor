package org.agilewiki.jactor.simpleMachine;

import junit.framework.TestCase;
import org.agilewiki.jactor.*;
import org.agilewiki.jactor.lpc.JLPCActor;

/**
 * Test code.
 */
public class CallTest extends TestCase {
    public void test() {
        MailboxFactory mailboxFactory = JAMailboxFactory.newMailboxFactory(1);
        try {
            Call actor = new Call();
            actor.initialize(mailboxFactory.createMailbox());
            JAFuture future = new JAFuture();
            System.out.println(SimpleRequest.req.send(future, actor));
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {
            mailboxFactory.close();
        }
    }

    class Call extends JLPCActor implements SimpleRequestReceiver {
        @Override
        public void processRequest(SimpleRequest request, RP rp)
                throws Exception {
            SMBuilder doubler = new SMBuilder();
            doubler._return(new ObjectFunc() {
                @Override
                public Object get(SimpleMachine sm) {
                    int req = ((Integer) sm.request).intValue();
                    return req * 2;
                }
            });

            SMBuilder main = new SMBuilder();
            main._call(doubler, new ObjectFunc() {
                @Override
                public Object get(SimpleMachine sm) {
                    return sm.request;
                }
            }, "rsp");
            main._return(new ObjectFunc() {
                @Override
                public Object get(SimpleMachine sm) {
                    return sm.get("rsp");
                }
            });

            main.call(3, rp);

            //Output:
            //6
        }
    }
}
