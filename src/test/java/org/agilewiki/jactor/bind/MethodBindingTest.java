package org.agilewiki.jactor.bind;

import junit.framework.TestCase;
import org.agilewiki.jactor.*;

/**
 * Test code.
 */
public class MethodBindingTest extends TestCase {
    public void test1() {
        System.err.println("test1");
        MailboxFactory mailboxFactory = JAMailboxFactory.newMailboxFactory(1);
        try {
            JBActor a = new A(mailboxFactory.createMailbox());
            Open.req.call(a);
            JAFuture future = new JAFuture();
            System.err.println((new Hi()).send(future, a));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mailboxFactory.close();
        }
    }

    public void test2() {
        System.err.println("test2");
        MailboxFactory mailboxFactory = JAMailboxFactory.newMailboxFactory(1);
        try {
            JBActor a = new A(mailboxFactory.createMailbox());
            Open.req.call(a);
            JBActor b = new JBActor(mailboxFactory.createMailbox());
            b.setParent(a);
            Open.req.call(b);
            JAFuture future = new JAFuture();
            System.err.println((new Hi()).send(future, b));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mailboxFactory.close();
        }
    }

    class Hi extends JBRequest<String> {
    }

    class A extends JBActor {

        A(Mailbox mailbox) {
            super(mailbox);

            bind(Hi.class.getName(), new MethodBinding<Hi, String>() {
                public void processRequest(Internals internals, Hi request, RP rp)
                        throws Exception {
                    rp.processResponse("Hello world!");
                }
            });
        }
    }
}
