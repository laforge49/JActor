package org.agilewiki.jactor.bufferedEvents;

import junit.framework.TestCase;
import org.agilewiki.jactor.concurrent.JAThreadManager;
import org.agilewiki.jactor.concurrent.ThreadManager;

final public class BufferedTimingTest extends TestCase {
    public void testTiming() {
        int c = 2;
        //int c = 100000;
        int b = 3;
        //int b = 1000;
        int p = 4;
        int t = 4;
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
        //4 parallel runs of 200000000 messages each.
        //800000000 messages sent with 4 threads.
        //msgs per sec = 81916854
        //12 nanoseconds per message
    }
}
