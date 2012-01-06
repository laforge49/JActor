package org.agilewiki.jactor.lpc.timing;

import junit.framework.TestCase;
import org.agilewiki.jactor.*;
import org.agilewiki.jactor.parallel.JAParallel;

public class SharedMailboxTest extends TestCase {
    public void testTiming() {
        int c = 2;
        int b = 3;
        int p = 1;
        int t = 1;

        //int c = 10000000;
        //int b = 1;
        //int p = 16;
        //int t = 4;

        //burst size of 1
        //16 parallel runs of 20000000 messages each.
        //320000000 messages sent with 4 threads.
        //msgs per sec = 518638573
        //1.9 nanoseconds per message test

        //int c = 2000000;
        //int b = 10;
        //int p = 16;
        //int t = 4;

        //burst size of 10
        //16 parallel runs of 40000000 messages each.
        //640000000 messages sent with 4 threads.
        //msgs per sec = 777642770
        //1.3 nanoseconds per message test

        //int c = 40000000;
        //int b = 1;
        //int p = 8;
        //int t = 4;

        //burst size of 1
        //8 parallel runs of 80000000 messages each.
        //640000000 messages sent with 4 threads.
        //msgs per sec = 506329113
        //2 nanoseconds per message test

        //int c = 100000;
        //int b = 1000;
        //int p = 4;
        //int t = 4;

        //burst size of 1000
        //4 parallel runs of 40000000 messages each.
        //800000000 messages sent with 4 threads.
        //msgs per sec = 730593607
        //1.4 nanosecond per message

        //int c = 20000;
        //int b = 1000;
        //int p = 16;
        //int t = 4;

        //burst size of 1000
        //16 parallel runs of 200000000 messages each.
        //640000000 messages sent with 4 threads.
        //msgs per sec = 1095890410
        //1 nanoseconds per message

        MailboxFactory mailboxFactory = JAMailboxFactory.newMailboxFactory(t);
        try {
            Actor[] senders = new Actor[p];
            int i = 0;
            while (i < p) {
                Mailbox sharedMailbox = mailboxFactory.createAsyncMailbox();
                Actor echo = new Echo(sharedMailbox);
                echo.setInitialBufferCapacity(b + 10);
                if (b == 1) senders[i] = new Sender1(sharedMailbox, echo, c, b);
                else senders[i] = new Sender(sharedMailbox, echo, c, b);
                senders[i].setInitialBufferCapacity(b + 10);
                i += 1;
            }
            JAParallel parallel = new JAParallel(mailboxFactory.createMailbox(), senders);
            JAFuture future = new JAFuture();
            future.send(parallel, future);
            future.send(parallel, future);
            long t0 = System.currentTimeMillis();
            future.send(parallel, future);
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
