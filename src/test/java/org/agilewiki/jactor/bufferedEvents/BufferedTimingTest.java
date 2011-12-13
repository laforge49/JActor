package org.agilewiki.jactor.bufferedEvents;

import junit.framework.TestCase;
import org.agilewiki.jactor.concurrent.JAThreadManager;
import org.agilewiki.jactor.concurrent.ThreadManager;

final public class BufferedTimingTest extends TestCase {
    public void testTiming() {
        int c = 10;
        //int c = 10000000; //c should be at least 10 million
        //int p = 1;
        int p = 2;
        //int t = 1;
        //int t = 2;
        //int t = 4;
        int t = 8;
        ThreadManager threadManager = JAThreadManager.newThreadManager(t);
        try {
            Sender sender = new Sender(threadManager, c);
            JABufferedEventsFuture<Object> eventFuture = new JABufferedEventsFuture<Object>();
            eventFuture.send(sender, eventFuture);
            eventFuture.send(sender, eventFuture);
            long t0 = System.currentTimeMillis();
            eventFuture.send(sender, eventFuture);
            long t1 = System.currentTimeMillis();
            System.out.println(""+p+" parallel runs of "+(2 * c)+" messages each.");
            System.out.println("" + (p * 2 * c) + " messages sent with " + t + " threads.");
            if (t1 != t0)
                System.out.println("msgs per sec = " + (c * p * 2L * 1000L / (t1 - t0)));
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
