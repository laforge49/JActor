package org.agilewiki.jactor.events.echoTiming;

import junit.framework.TestCase;
import org.agilewiki.jactor.concurrent.JAThreadManager;
import org.agilewiki.jactor.concurrent.ThreadManager;
import org.agilewiki.jactor.events.JAEventFuture;
import org.agilewiki.jactor.events.Sender;

public class EchoTimingTest extends TestCase {
    public void testTiming() {
        //int c = 10;
        int c = 10000000; //c should be at least 10 million
        int t = 1;
        //int t = 2;
        //int t = 4;
        //int t = 8;
        ThreadManager threadManager = JAThreadManager.newThreadManager(t);
        try {
            Sender sender = new Sender(threadManager, c);
            JAEventFuture<Object> eventFuture = new JAEventFuture<Object>();
            long t0 = System.currentTimeMillis();
            eventFuture.send(sender, eventFuture);
            long t1 = System.currentTimeMillis();
            System.out.println(""+(2*c)+" messages sent with "+t+" threads");
            if (t1 != t0)
                System.out.println("msgs per sec = " + (c * 2L * 1000L / (t1 - t0)));
        } finally {
            threadManager.close();
        }
        //20000000 messages sent with 1 threads
        //msgs per sec = 8113590
        //123 nanoseconds per message

        //20000000 messages sent with 2 threads
        //msgs per sec = 3936233
        //254 nanoseconds per message

        //20000000 messages sent with 4 threads
        //msgs per sec = 3920031
        //255 nanoseconds per message

        //20000000 messages sent with 8 threads
        //msgs per sec = 3953350
        //252 nanoseconds per message
    }
}
