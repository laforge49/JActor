package org.agilewiki.jactor.synchronousProgrammingTest;

import junit.framework.TestCase;
import org.agilewiki.jactor.JAFuture;
import org.agilewiki.jactor.JAMailboxFactory;
import org.agilewiki.jactor.Mailbox;
import org.agilewiki.jactor.bind.Open;
import org.agilewiki.jactor.components.Include;
import org.agilewiki.jactor.components.JCActor;

public class GreeterTest extends TestCase {
    public void test1() {
        System.out.println("start GreeterTest 1");
        JAMailboxFactory mailboxFactory = JAMailboxFactory.newMailboxFactory(1);
        try {
            Mailbox mailbox = mailboxFactory.createMailbox();
            JCActor a = new JCActor(mailbox);
            (new Include(Greeter.class)).call(a);
            Open.req.call(a);
            JAFuture future = new JAFuture();
            String greeting = (new Hi()).send(future, a);
            System.out.println(greeting);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println("end GreeterTest 1");
            mailboxFactory.close();
        }
    }

    public void test2() {
        System.out.println("start GreeterTest 2");
        JAMailboxFactory mailboxFactory = JAMailboxFactory.newMailboxFactory(1);
        try {
            Mailbox mailbox = mailboxFactory.createMailbox();
            JCActor a = new JCActor(mailbox);
            (new Include(Greeter.class)).call(a);
            Open.req.call(a);
            JCActor b = new JCActor(mailbox);
            (new Include(ResponsePrinter.class)).call(b);
            Open.req.call(b);
            JAFuture future = new JAFuture();
            (new PrintResponse(new Hi(), a)).send(future, b);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println("end GreeterTest 2");
            mailboxFactory.close();
        }
    }

    public void test3() {
        System.out.println("start GreeterTest 3");
        JAMailboxFactory mailboxFactory = JAMailboxFactory.newMailboxFactory(1);
        try {
            Mailbox mailbox = mailboxFactory.createMailbox();
            JCActor a = new JCActor(mailbox);
            (new Include(Greeter.class)).call(a);
            (new Include(ResponsePrinter.class)).call(a);
            Open.req.call(a);
            JAFuture future = new JAFuture();
            (new PrintResponse(new Hi(), a)).send(future, a);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println("end GreeterTest 3");
            mailboxFactory.close();
        }
    }

    public void test4() {
        System.out.println("start GreeterTest 4");
        JAMailboxFactory mailboxFactory = JAMailboxFactory.newMailboxFactory(1);
        try {
            Mailbox mailbox = mailboxFactory.createMailbox();
            JCActor a = new JCActor(mailbox);
            (new Include(Greeter.class)).call(a);
            Open.req.call(a);
            JCActor b = new JCActor(mailbox);
            b.setParent(a);
            (new Include(ResponsePrinter.class)).call(b);
            Open.req.call(b);
            JAFuture future = new JAFuture();
            (new PrintResponse(new Hi(), b)).send(future, b);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println("end GreeterTest 4");
            mailboxFactory.close();
        }
    }
}
