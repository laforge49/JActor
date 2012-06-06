package org.agilewiki.jactor.synchronousProgrammingTest;

import junit.framework.TestCase;
import org.agilewiki.jactor.JAFuture;
import org.agilewiki.jactor.JAMailboxFactory;
import org.agilewiki.jactor.Mailbox;

/**
 * Test code.
 */
public class GreeterTest extends TestCase {
    public void test1() {
        System.out.println("start GreeterTest 1");
        JAMailboxFactory mailboxFactory = JAMailboxFactory.newMailboxFactory(1);
        try {
            Mailbox mailbox = mailboxFactory.createMailbox();
            Greeter a = new Greeter();
            a.initialize(mailbox);
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
            Greeter a = new Greeter();
            a.initialize(mailbox);
            ResponsePrinter b = new ResponsePrinter();
            b.initialize(mailbox);
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
            Greeter a = new Greeter();
            a.initialize(mailbox);
            ResponsePrinter b = new ResponsePrinter();
            b.initialize(mailbox, a);
            JAFuture future = new JAFuture();
            (new PrintResponse(new Hi(), b)).send(future, b);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println("end GreeterTest 3");
            mailboxFactory.close();
        }
    }
}
