package org.agilewiki.jactor.bind;

import junit.framework.TestCase;
import org.agilewiki.jactor.*;

public class MethodBindingTest extends TestCase {
    public void test1() {
        System.err.println("test1");
        MailboxFactory mailboxFactory = JAMailboxFactory.newMailboxFactory(1);
        try {
            Actor a = new A(mailboxFactory.createMailbox());
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
            Actor a = new A(mailboxFactory.createMailbox());
            JBActor b = new JBActor(mailboxFactory.createMailbox());
            b.setParent(a);
            JAFuture future = new JAFuture();
            System.err.println((new Hi()).send(future, b));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mailboxFactory.close();
        }
    }

    class Hi extends Request<String> {
    }

    class A extends JBActor {

        A(Mailbox mailbox) {
            super(mailbox);

            bind(Hi.class.getName(), new MethodBinding<Hi, Object>() {
                public void processRequest(Internals internals, Hi request, RP rp)
                        throws Exception {
                    rp.process("Hello world!");
                }
            });
        }
    }
}
