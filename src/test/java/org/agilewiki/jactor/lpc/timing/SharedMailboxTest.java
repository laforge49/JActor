package org.agilewiki.jactor.lpc.timing;

import junit.framework.TestCase;
import org.agilewiki.jactor.concurrent.JAThreadManager;
import org.agilewiki.jactor.concurrent.ThreadManager;
import org.agilewiki.jactor.lpc.JLPCFuture;
import org.agilewiki.jactor.lpc.JLPCMailbox;
import org.agilewiki.jactor.lpc.LPCActor;
import org.agilewiki.jactor.lpc.LPCMailbox;

public class SharedMailboxTest extends TestCase {
    public void testTiming() {
        int c = 2;
        int b = 3;
        int p = 1;
        int t = 1;

        //int c = 20000000;
        //int b = 1;
        //int p = 16;
        //int t = 4;

        //burst size of 1
        //16 parallel runs of 40000000 messages each.
        //640000000 messages sent with 4 threads.
        //msgs per sec = 378474275
        //2.6 nanoseconds per message --slowed down a bit by JIterator.

        //int c = 80000000;
        //int b = 1;
        //int p = 8;
        //int t = 4;

        //burst size of 1
        //8 parallel runs of 160000000 messages each.
        //1280000000 messages sent with 4 threads.
        //msgs per sec = 284697508
        //3.5 nanoseconds per message

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

        ThreadManager threadManager = JAThreadManager.newThreadManager(t);
        try {
            LPCActor[] senders = new LPCActor[p];
            int i = 0;
            while (i < p) {
                LPCMailbox sharedMailbox = new JLPCMailbox(threadManager, true);
                LPCActor echo = new Echo(sharedMailbox);
                echo.setInitialBufferCapacity(b + 10);
                senders[i] = new Sender(sharedMailbox, echo, c, b);
                senders[i].setInitialBufferCapacity(b + 10);
                i += 1;
            }
            Driver driver = new Driver(new JLPCMailbox(threadManager), senders, p);
            JLPCFuture future = new JLPCFuture();
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
            threadManager.close();
        }
    }
}
