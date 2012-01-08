package org.agilewiki.jactor.bind;

import junit.framework.TestCase;
import org.agilewiki.jactor.*;

public class DataBindingTest extends TestCase {
    public void test1() {
        System.err.println("test1");
        MailboxFactory mailboxFactory = JAMailboxFactory.newMailboxFactory(1);
        try {
            Actor a = new A(mailboxFactory.createMailbox());
            JAFuture future = new JAFuture();
            System.err.println(future.send(a, new Hi()));
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
            System.err.println(future.send(b, new Hi()));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mailboxFactory.close();
        }
    }

    class Hi {
    }

    class A extends JBActor {

        A(Mailbox mailbox) {
            super(mailbox);
            
            internals.data.put("greeting", "Hello world!");

            bind(Hi.class.getName(), new DataBinding("greeting"));
        }
    }
}
