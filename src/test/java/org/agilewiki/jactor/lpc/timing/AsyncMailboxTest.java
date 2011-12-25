package org.agilewiki.jactor.lpc.timing;

import junit.framework.TestCase;
import org.agilewiki.jactor.*;

public class AsyncMailboxTest extends TestCase {
    public void testTiming() {
        int c = 2;
        int b = 3;
        int p = 1;
        int t = 1;

        //int c = 30000;
        //int b = 1000;
        //int p = 4;
        //int t = 4;

        //burst size of 1000
        //4 parallel runs of 200000000 messages each.
        //800000000 messages sent with 4 threads.
        //msgs per sec = 48221820
        //22 nanoseconds per message

        MailboxFactory mailboxFactory = JAMailboxFactory.newMailboxFactory(t);
        try {
            Actor[] senders = new Actor[p];
            int i = 0;
            while (i < p) {
                Mailbox echoMailbox = mailboxFactory.createAsyncMailbox();
                Actor echo = new Echo(echoMailbox);
                echo.setInitialBufferCapacity(b + 10);
                Mailbox senderMailbox = mailboxFactory.createAsyncMailbox();
                if (b == 1) senders[i] = new Sender1(senderMailbox, echo, c, b);
                else senders[i] = new Sender(senderMailbox, echo, c, b);
                senders[i].setInitialBufferCapacity(b + 10);
                i += 1;
            }
            Driver driver = new Driver(mailboxFactory.createAsyncMailbox(), senders, p);
            JAFuture future = new JAFuture();
            future.send(driver, future);
            future.send(driver, future);
            long t0 = System.currentTimeMillis();
            future.send(driver, future);
            long t1 = System.currentTimeMillis();
            System.out.println("" + p + " parallel runs of " + (2L * c * b) + " messages each.");
            System.out.println("" + (2L * c * b * p) + " messages sent with " + t + " threads.");
            if (t1 != t0)
                System.out.println("msgs per sec = " + ((2L * c * b * p) * 1000L / (t1 - t0)));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mailboxFactory.close();
        }
    }
}
