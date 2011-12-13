package org.agilewiki.jactor.events.dualEchoTiming;

import junit.framework.TestCase;
import org.agilewiki.jactor.concurrent.JAThreadManager;
import org.agilewiki.jactor.concurrent.ThreadManager;
import org.agilewiki.jactor.events.JAEventFuture;
import org.agilewiki.jactor.events.Sender;

final public class DualEchoTimingTest extends TestCase {
    public void testTiming() {
        int c = 10;
        //int c = 10000000; //c should be at least 10 million
        //int t = 1;
        //int t = 2;
        //int t = 4;
        int t = 8;
        ThreadManager threadManager = JAThreadManager.newThreadManager(t);
        try {
            Driver driver = new Driver(threadManager, c);
            JAEventFuture<Object> eventFuture = new JAEventFuture<Object>();
            eventFuture.send(driver, eventFuture);
            eventFuture.send(driver, eventFuture);
            long t0 = System.currentTimeMillis();
            eventFuture.send(driver, eventFuture);
            long t1 = System.currentTimeMillis();
            System.out.println("" + (2 * 2 * c) + " messages sent with " + t + " threads");
            if (t1 != t0)
                System.out.println("msgs per sec = " + (c * 2L * 2L * 1000L / (t1 - t0)));
        } finally {
            threadManager.close();
        }
        //40000000 messages sent with 1 threads
        //msgs per sec = 8863283
        //113 nanoseconds per message

        //40000000 messages sent with 2 threads
        //msgs per sec = 4758505
        //210 nanoseconds per message

        //40000000 messages sent with 4 threads
        //msgs per sec = 4487826
        //223 nanoseconds per message

        //40000000 messages sent with 8 threads
        //msgs per sec = 4437049
        //225 nanoseconds per message
    }
}
