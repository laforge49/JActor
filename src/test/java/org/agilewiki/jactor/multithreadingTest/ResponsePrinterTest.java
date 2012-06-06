package org.agilewiki.jactor.multithreadingTest;

import junit.framework.TestCase;
import org.agilewiki.jactor.JAFuture;
import org.agilewiki.jactor.JAMailboxFactory;
import org.agilewiki.jactor.Mailbox;

/**
 * Test code.
 */
public class ResponsePrinterTest extends TestCase {
    public void test1() {
        System.out.println("start ResponsePrinterTest 1");
        JAMailboxFactory mailboxFactory = JAMailboxFactory.newMailboxFactory(10);
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
            System.out.println("end ResponsePrinterTest 1");
            mailboxFactory.close();
        }
    }

    public void test2() {
        System.out.println("start ResponsePrinterTest 2");
        JAMailboxFactory mailboxFactory = JAMailboxFactory.newMailboxFactory(10);
        try {
            Mailbox mailbox1 = mailboxFactory.createMailbox();
            Mailbox mailbox2 = mailboxFactory.createMailbox();
            Greeter a = new Greeter();
            a.initialize(mailbox1);
            ResponsePrinter b = new ResponsePrinter();
            b.initialize(mailbox2);
            JAFuture future = new JAFuture();
            (new PrintResponse(new Hi(), a)).send(future, b);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println("end ResponsePrinterTest 2");
            mailboxFactory.close();
        }
    }

    public void test3() {
        System.out.println("start ResponsePrinterTest 3");
        JAMailboxFactory mailboxFactory = JAMailboxFactory.newMailboxFactory(10);
        try {
            Mailbox mailbox1 = mailboxFactory.createMailbox();
            Mailbox mailbox2 = mailboxFactory.createAsyncMailbox();
            Greeter a = new Greeter();
            a.initialize(mailbox1);
            ResponsePrinter b = new ResponsePrinter();
            b.initialize(mailbox2);
            JAFuture future = new JAFuture();
            (new PrintResponse(new Hi(), a)).send(future, b);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println("end ResponsePrinterTest 3");
            mailboxFactory.close();
        }
    }

    public void test4() {
        System.out.println("start ResponsePrinterTest 4");
        JAMailboxFactory mailboxFactory = JAMailboxFactory.newMailboxFactory(10);
        try {

            Greeter a = new Greeter();
            a.initialize(mailboxFactory.createMailbox());
            int count = 5;
            ResponsePrinter[] bs = new ResponsePrinter[count];
            int i = 0;
            while (i < count) {
                ResponsePrinter b = new ResponsePrinter();
                b.initialize(mailboxFactory.createAsyncMailbox());
                bs[i] = b;
                i += 1;
            }
            ParallelResponsePrinter c = new ParallelResponsePrinter();
            c.initialize(mailboxFactory.createMailbox());
            JAFuture future = new JAFuture();
            PrintResponse printResponse = new PrintResponse(new Hi(), a);
            PrintParallelResponse printParallelResponse = new PrintParallelResponse(count, bs, printResponse);
            printParallelResponse.send(future, c);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println("end ResponsePrinterTest 4");
            mailboxFactory.close();
        }
    }

    public void test5() {
        System.out.println("start ResponsePrinterTest 5");
        JAMailboxFactory mailboxFactory = JAMailboxFactory.newMailboxFactory(10);
        try {

            Greeter a = new Greeter();
            a.initialize(mailboxFactory.createMailbox());
            int count = 5;
            ResponsePrinter[] bs = new ResponsePrinter[count];
            int i = 0;
            while (i < count) {
                ResponsePrinter b = new ResponsePrinter();
                b.initialize(mailboxFactory.createAsyncMailbox());
                bs[i] = b;
                i += 1;
            }
            ParallelResponsePrinter c = new ParallelResponsePrinter();
            c.initialize(mailboxFactory.createMailbox());
            JAFuture future = new JAFuture();
            PrintResponse printResponse = new PrintResponse(new Hi(), a);
            PrintParallelResponse printParallelResponse = new PrintParallelResponse(count, bs, printResponse);
            int j = 0;
            while (j < 1) {
                printParallelResponse.send(future, c);
                j += 1;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println("end ResponsePrinterTest 5");
            mailboxFactory.close();
        }
    }
}
