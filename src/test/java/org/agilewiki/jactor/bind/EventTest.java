package org.agilewiki.jactor.bind;

import junit.framework.TestCase;
import org.agilewiki.jactor.*;

/**
 * Test code.
 */
public class EventTest extends TestCase {
    public void test() {
        MailboxFactory mailboxFactory = JAMailboxFactory.newMailboxFactory(1);
        try {
            JBActor a = new A(mailboxFactory.createMailbox());
            Open.req.call(a);
            JAFuture future = new JAFuture();
            Hi hi = new Hi();
            hi.sendEvent(a);
            hi.send(future, a);
            Ho ho = new Ho();
            ho.sendEvent(a);
            ho.call(a);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mailboxFactory.close();
        }
    }

    class Hi extends Request<String> {
    }

    class Ho extends JBConcurrentRequest<String> {
    }

    class A extends JBActor {

        A(Mailbox mailbox) {
            super(mailbox);

            bind(Hi.class.getName(), new MethodBinding<Hi, String>() {
                public void processRequest(Internals internals, Hi request, RP rp)
                        throws Exception {
                    System.err.println("A got request");
                    rp.processResponse("Hello world!");
                }
            });

            bind(Ho.class.getName(), new ConcurrentMethodBinding<Ho, String>() {
                @Override
                public String concurrentProcessRequest(RequestReceiver requestReceiver,
                                                       Ho request)
                        throws Exception {
                    System.err.println("A got request");
                    return "Hello world!";
                }
            });
        }
    }
}
