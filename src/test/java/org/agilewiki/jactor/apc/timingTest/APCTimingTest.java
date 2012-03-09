package org.agilewiki.jactor.apc.timingTest;

import junit.framework.TestCase;
import org.agilewiki.jactor.apc.JAPCFuture;
import org.agilewiki.jactor.concurrent.JAThreadManager;
import org.agilewiki.jactor.concurrent.ThreadManager;

/**
 * Test code.
 */
final public class APCTimingTest extends TestCase {
    public void testTiming() {
        int c = 2;
        int b = 3;
        int p = 1;
        int t = 1;

        //int c = 100000;
        //int b = 1000;
        //int p = 4;
        //int t = 4;

        //burst size of 1000
        //4 parallel runs of 200000000 messages each.
        //800000000 messages sent with 4 threads.
        //msgs per sec = 49578582
        //20 nanoseconds per message

        //int c = 25000;
        //int b = 1000;
        //int p = 16;
        //int t = 4;

        //burst size of 1000
        //16 parallel runs of 50000000 messages each.
        //800000032 messages sent with 4 threads.
        //msgs per sec = 49489638
        //20 nanoseconds per message

        //int c = 1000000;
        //int b = 1;
        //int p = 16;
        //int t = 4;

        //burst size of 1
        //16 parallel runs of 2000000 messages each.
        //32000000 messages sent with 4 threads.
        //msgs per sec = 4255319
        //235 nanoseconds per message

        ThreadManager threadManager = JAThreadManager.newThreadManager(t);
        try {
            Driver driver = new Driver(threadManager, c, b, p);
            JAPCFuture eventFuture = new JAPCFuture();
            eventFuture.send(driver, eventFuture);
            eventFuture.send(driver, eventFuture);
            long t0 = System.currentTimeMillis();
            eventFuture.send(driver, eventFuture);
            long t1 = System.currentTimeMillis();
            System.out.println("" + p + " driver runs of " + (2 * c * b) + " messages each.");
            System.out.println("" + (p * 2 * c * b) + " messages sent with " + t + " threads.");
            if (t1 != t0)
                System.out.println("msgs per sec = " + ((c * b * p * 2L) * 1000L / (t1 - t0)));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            threadManager.close();
        }
    }
}
