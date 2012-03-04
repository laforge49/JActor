package org.agilewiki.jactor.bind;

import junit.framework.TestCase;
import org.agilewiki.jactor.JAMailboxFactory;
import org.agilewiki.jactor.Mailbox;
import org.agilewiki.jactor.MailboxFactory;

public class ConcurrentDataBindingTest extends TestCase {
    public void test1() {
        System.err.println("test1");
        MailboxFactory mailboxFactory = JAMailboxFactory.newMailboxFactory(1);
        try {
            JBActor a = new A(mailboxFactory.createMailbox());
            Open.req.call(a);
            System.err.println((new Hi()).call(a));
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
            System.err.println((new Hi()).call(b));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mailboxFactory.close();
        }
    }

    class Hi extends ConcurrentRequest<String> {
    }

    class A extends JBActor {

        A(Mailbox mailbox) {
            super(mailbox);

            getData().put("greeting", "Hello world!");

            bind(Hi.class.getName(), new ConcurrentDataBinding<Hi, String>("greeting"));
        }
    }
}
