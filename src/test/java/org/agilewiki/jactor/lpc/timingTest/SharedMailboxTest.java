package org.agilewiki.jactor.lpc.timingTest;

import junit.framework.TestCase;
import org.agilewiki.jactor.*;
import org.agilewiki.jactor.parallel.JAParallel;

/**
 * Test code.
 */
public class SharedMailboxTest extends TestCase {
    public void testTiming() {
        int c = 1;
        int b = 1;
        int p = 1;
        int t = 1;

        //int c = 500000000;
        //int b = 1;
        //int p = 1;
        //int t = 1;

        //burst size of 1
        //1 parallel runs of 1000000000 messages each.
        //1000000000 messages sent with 1 threads.
        //msgs per sec = 311138767
        //3.2 nanoseconds per message
        //3.2 nanosecond latency
        //8 clock cycle latency

        //int c = 100000;
        //int b = 1000;
        //int p = 16;
        //int t = 4;

        //burst size of 1000
        //16 parallel runs of 200000000 messages each.
        //3200000000 messages sent with 4 threads.
        //msgs per sec = 1,005,340,873 (now 136,123,872)
        //.99 nanoseconds per message
        //5 clock cycles per message

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
            future.send(parallel, null);
            long t2 = System.currentTimeMillis();
            System.out.println("null test time " + (t2 - t1));
            System.out.println("" + p + " parallel runs of " + (2L * c * b) + " messages each.");
            System.out.println("" + (2L * c * b * p) + " messages sent with " + t + " threads.");
            if (t1 != t0 && t1 - t0 - t2 + t1 > 0) {
                System.out.println("msgs per sec = " + ((2L * c * b * p) * 1000L / (t1 - t0)));
                System.out.println("adjusted msgs per sec = " + ((2L * c * b * p) * 1000L / (t1 - t0 - t2 + t1)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mailboxFactory.close();
        }
    }
}
