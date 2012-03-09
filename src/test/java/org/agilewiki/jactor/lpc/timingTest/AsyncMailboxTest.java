package org.agilewiki.jactor.lpc.timingTest;

import junit.framework.TestCase;
import org.agilewiki.jactor.*;
import org.agilewiki.jactor.parallel.JAParallel;

public class AsyncMailboxTest extends TestCase {
    public void testTiming() {
        int c = 2;
        int b = 3;
        int p = 1;
        int t = 1;

        //int c = 5000000;
        //int b = 1;
        //int p = 1;
        //int t = 1;

        //burst size of 1
        //1 parallel runs of 10000000 messages each.
        //10000000 messages sent with 1 threads.
        //msgs per sec = 1953888
        //512 nanoseconds per message
        //512 nanosecond latency
        //1295 clock cycle latency

        //int c = 50000;
        //int b = 1000;
        //int p = 4;
        //int t = 4;

        //burst size of 1000
        //4 parallel runs of 100000000 messages each.
        //400000000 messages sent with 4 threads.
        //msgs per sec = 43080236
        //23 nanoseconds per message
        //116 clock cycles per message

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
            JAParallel parallel = new JAParallel(mailboxFactory.createAsyncMailbox(), senders);
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
            if (t1 != t0) {
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
