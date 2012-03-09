package org.agilewiki.jactor.lpc;

import junit.framework.TestCase;
import org.agilewiki.jactor.*;

/**
 * Test code.
 */
public class EventTest extends TestCase {
    public void test() {
        MailboxFactory mailboxFactory = JAMailboxFactory.newMailboxFactory(1);
        try {
            Actor a = new A(mailboxFactory.createMailbox());
            JAEvent event = new JAEvent();
            JAFuture future = new JAFuture();
            event.sendEvent(a, null);
            future.send(a, null);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mailboxFactory.close();
        }
    }

    class A extends JLPCActor {

        A(Mailbox mailbox) {
            super(mailbox);
        }

        @Override
        public void processRequest(Object request, RP rp) throws Exception {
            System.err.println("A got request");
            rp.processResponse(request);
        }
    }
}
