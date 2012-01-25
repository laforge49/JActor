package org.agilewiki.jactor.lpc.calculator;

import junit.framework.TestCase;
import org.agilewiki.jactor.*;

public class CalculatorTest extends TestCase {
    public void test1() {
        System.err.println("test 1");
        MailboxFactory mailboxFactory = JAMailboxFactory.newMailboxFactory(1);
        try {
            Mailbox mailbox = mailboxFactory.createMailbox();
            Actor calculator = new Calculator(mailbox);
            JAFuture future = new JAFuture();
            future.send(calculator, new Set(1));
            future.send(calculator, new Add(2));
            System.err.println(future.send(calculator, new Multiply(3)));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mailboxFactory.close();
        }
    }

    public void test2() {
        System.err.println("test 2");
        MailboxFactory mailboxFactory = JAMailboxFactory.newMailboxFactory(1);
        try {
            Mailbox mailbox = mailboxFactory.createMailbox();
            Actor driver = new Driver1(mailbox);
            JAFuture future = new JAFuture();
            System.err.println(future.send(driver, null));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mailboxFactory.close();
        }
    }

    public void test3() {
        System.err.println("test 3");
        MailboxFactory mailboxFactory = JAMailboxFactory.newMailboxFactory(1);
        try {
            Mailbox mailbox = mailboxFactory.createMailbox();
            Actor driver = new Driver2(mailbox);
            JAFuture future = new JAFuture();
            System.err.println(future.send(driver, null));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mailboxFactory.close();
        }
    }

    public void test4() {
        System.err.println("test 4");
        MailboxFactory mailboxFactory = JAMailboxFactory.newMailboxFactory(1);
        try {
            Mailbox mailbox = mailboxFactory.createMailbox();
            Actor driver = new Driver3(mailbox);
            JAFuture future = new JAFuture();
            System.err.println(future.send(driver, null));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mailboxFactory.close();
        }
    }

    public void test5() {
        System.err.println("test 5");
        MailboxFactory mailboxFactory = JAMailboxFactory.newMailboxFactory(1);
        try {
            Mailbox mailbox = mailboxFactory.createMailbox();
            Actor calculator = new PrintingCalculator(mailbox);
            JAFuture future = new JAFuture();
            future.send(calculator, new Set(1));
            future.send(calculator, new Add(2));
            future.send(calculator, new Multiply(3));
            future.send(calculator, new Divide(0));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mailboxFactory.close();
        }
    }

    public void test6() {
        System.err.println("test 6");
        MailboxFactory mailboxFactory = JAMailboxFactory.newMailboxFactory(1);
        try {
            Mailbox mailbox = mailboxFactory.createMailbox();
            Actor calculator = new FactorialCalculation(mailbox);
            JAFuture future = new JAFuture();
            System.err.println(future.send(calculator, new Factorial(5)));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mailboxFactory.close();
        }
    }
}
