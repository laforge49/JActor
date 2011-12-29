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

        //int c = 40000000;
        //int b = 1;
        //int p = 16;
        //int t = 4;

        //burst size of 1
        //16 parallel runs of 80000000 messages each.
        //1280000000 messages sent with 4 threads.
        //msgs per sec = 538720538
        //1.9 nanoseconds per message test
        //-.65 nanosecond for JAIterator
        //= 1.2 nanoseconds per message

        //int c = 2000000;
        //int b = 10;
        //int p = 16;
        //int t = 4;

        //burst size of 10
        //16 parallel runs of 40000000 messages each.
        //640000000 messages sent with 4 threads.
        //msgs per sec = 676532769
        //1.5 nanoseconds per message test
        //-.1 nanoseconds for JAIterator
        //-.2 nanoseconds for Sender overhead
        //=1.2 nanoseconds per message.

        //int c = 80000000;
        //int b = 1;
        //int p = 8;
        //int t = 4;

        //burst size of 1
        //8 parallel runs of 160000000 messages each.
        //1280000000 messages sent with 4 threads.
        //msgs per sec = 542143159
        //1.8 nanoseconds per message test
        //-.65 nanoseconds for JAIterator
        //=1.2 nanoseconds per message.

        //int c = 500000;
        //int b = 1000;
        //int p = 4;
        //int t = 4;

        //burst size of 1000
        //4 parallel runs of 1000000000 messages each.
        //4000000000 messages sent with 4 threads.
        //msgs per sec = 829703381
        //1.2 nanosecond per message

        //int c = 100000;
        //int b = 1000;
        //int p = 16;
        //int t = 4;

        //burst size of 1000
        //16 parallel runs of 200000000 messages each.
        //3200000000 messages sent with 4 threads.
        //msgs per sec = 847457627
        //1.2 nanoseconds per message

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
