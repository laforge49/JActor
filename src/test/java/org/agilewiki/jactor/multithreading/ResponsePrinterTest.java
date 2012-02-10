package org.agilewiki.jactor.multithreading;

import junit.framework.TestCase;
import org.agilewiki.jactor.JAFuture;
import org.agilewiki.jactor.JAMailboxFactory;
import org.agilewiki.jactor.Mailbox;
import org.agilewiki.jactor.components.Include;
import org.agilewiki.jactor.components.JCActor;
import org.agilewiki.jactor.synchronousProgramming.Greeter;
import org.agilewiki.jactor.synchronousProgramming.Hi;

public class ResponsePrinterTest extends TestCase {
    public void test1() {
        System.out.println("start ResponsePrinterTest 1");
        JAMailboxFactory mailboxFactory = JAMailboxFactory.newMailboxFactory(10);
        try {
            Mailbox mailbox = mailboxFactory.createMailbox();
            JCActor a = new JCActor(mailbox);
            (new Include(Greeter.class)).call(a);
            JCActor b = new JCActor(mailbox);
            (new Include(ResponsePrinter.class)).call(b);
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
            JCActor a = new JCActor(mailbox1);
            (new Include(Greeter.class)).call(a);
            JCActor b = new JCActor(mailbox2);
            (new Include(ResponsePrinter.class)).call(b);
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
            JCActor a = new JCActor(mailbox1);
            (new Include(Greeter.class)).call(a);
            JCActor b = new JCActor(mailbox2);
            (new Include(ResponsePrinter.class)).call(b);
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

            JCActor a = new JCActor(mailboxFactory.createMailbox());

            (new Include(Greeter.class)).call(a);
            int count = 5;
            JCActor[] bs = new JCActor[count];
            int i = 0;
            while (i < count) {
                JCActor b = new JCActor(mailboxFactory.createAsyncMailbox());
                (new Include(ResponsePrinter.class)).call(b);
                bs[i] = b;
                i += 1;
            }
            JCActor c = new JCActor(mailboxFactory.createMailbox());
            (new Include(ParallelResponsePrinter.class)).call(c);
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
}
