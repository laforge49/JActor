package org.agilewiki.jactor.bufferedEvents;

import junit.framework.TestCase;
import org.agilewiki.jactor.concurrent.JAThreadManager;
import org.agilewiki.jactor.concurrent.ThreadManager;

/**
 * Test code.
 */
final public class BufferedTimingTest extends TestCase {
    public void testTiming() {
        int c = 2;
        int b = 3;
        int p = 4;
        int t = 4;

        //int c = 100000;
        //int b = 1000;
        //int p = 4;
        //int t = 4;

        //int c = 50000;
        //int b = 1000;
        //int p = 8;
        //int t = 8;

        //int c = 25000;
        //int b = 1000;
        //int p = 16;
        //int t = 16;

        //int c = 50000;
        //int b = 1000;
        //int p = 8;
        //int t = 4;

        //int c = 25000;
        //int b = 1000;
        //int p = 16;
        //int t = 4;

        //int c = 2500;
        //int b = 1000;
        //int p = 160;
        //int t = 4;

        //int c = 1000000;
        //int b = 1;
        //int p = 16;
        //int t = 4;

        ThreadManager threadManager = JAThreadManager.newThreadManager(t);
        try {
            Driver driver = new Driver(threadManager, c, b, p);
            JABufferedEventsFuture<Object> eventFuture = new JABufferedEventsFuture<Object>();
            eventFuture.send(driver, eventFuture);
            eventFuture.send(driver, eventFuture);
            long t0 = System.currentTimeMillis();
            eventFuture.send(driver, eventFuture);
            long t1 = System.currentTimeMillis();
            System.out.println("" + p + " parallel runs of " + (2 * c * b) + " messages each.");
            System.out.println("" + (p * 2 * c * b + 2 * p) + " messages sent with " + t + " threads.");
            if (t1 != t0)
                System.out.println("msgs per sec = " + ((c * b * p * 2L + 2L * p) * 1000L / (t1 - t0)));
        } finally {
            threadManager.close();
        }

        //burst size of 1000
        //4 parallel runs of 200000000 messages each.
        //800000008 messages sent with 4 threads.
        //msgs per sec = 82987551
        //12 nanoseconds per message

        //burst size of 1000
        //8 parallel runs of 100000000 messages each.
        //800000016 messages sent with 8 threads.
        //msgs per sec = 80256821
        //12 nanoseconds per message

        //burst size of 1000
        //16 parallel runs of 50000000 messages each.
        //800000032 messages sent with 16 threads.
        //msgs per sec = 83246618
        //12 nanoseconds per message

        //burst size of 1000
        //8 parallel runs of 100000000 messages each.
        //800000016 messages sent with 4 threads.
        //msgs per sec = 83927822
        //12 nanoseconds per message

        //burst size of 1000
        //16 parallel runs of 50000000 messages each.
        //800000032 messages sent with 4 threads.
        //msgs per sec = 85616438
        //12 nanoseconds per message

        //burst size of 1000
        //160 parallel runs of 5000000 messages each.
        //800000320 messages sent with 4 threads.
        //msgs per sec = 82584940
        //12 nanoseconds per message

        //burst size of 1
        //16 parallel runs of 2000000 messages each.
        //32000032 messages sent with 4 threads.
        //msgs per sec = 4468654
        //224 nanoseconds per message
    }
}
