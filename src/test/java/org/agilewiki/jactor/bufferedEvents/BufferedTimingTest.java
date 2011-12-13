package org.agilewiki.jactor.bufferedEvents;

import junit.framework.TestCase;
import org.agilewiki.jactor.concurrent.JAThreadManager;
import org.agilewiki.jactor.concurrent.ThreadManager;

final public class BufferedTimingTest extends TestCase {
    public void testTiming() {
        //int c = 10;
        int c = 10000000;
        int b = 1;
        //int b = 10000;
        int p = 1;
        //int p = 2;
        //int p = 4;
        int t = 1;
        //int t = 2;
        //int t = 4;
        //int t = 8;
        ThreadManager threadManager = JAThreadManager.newThreadManager(t);
        try {
            Driver driver = new Driver(threadManager, c, b, p);
            JABufferedEventsFuture<Object> eventFuture = new JABufferedEventsFuture<Object>();
            eventFuture.send(driver, eventFuture);
            eventFuture.send(driver, eventFuture);
            long t0 = System.currentTimeMillis();
            eventFuture.send(driver, eventFuture);
            long t1 = System.currentTimeMillis();
            System.out.println(""+p+" parallel runs of "+(2 * c * b)+" messages each.");
            System.out.println("" + (p * 2 * c * b) + " messages sent with " + t + " threads.");
            if (t1 != t0)
                System.out.println("msgs per sec = " + (c * b * p * 2L * 1000L / (t1 - t0)));
        } finally {
            threadManager.close();
        }
        //2 parallel runs of 40000000 messages each.
        //40000000 messages sent with 1 threads.
        //msgs per sec = 16757436
        //60 nanoseconds per message

        //2 parallel runs of 40000000 messages each.
        //40000000 messages sent with 2 threads.
        //msgs per sec = 6920415
        //144 nanoseconds per message

        //2 parallel runs of 40000000 messages each.
        //40000000 messages sent with 4 threads.
        //msgs per sec = 7853917
        //127 nanoseconds per message

        //2 parallel runs of 40000000 messages each.
        //40000000 messages sent with 8 threads.
        //msgs per sec = 7524454
        //133 nanoseconds per message
    }
}
