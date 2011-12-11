package org.agilewiki.jactor.events.echoTiming;

import org.agilewiki.jactor.concurrent.JAThreadFactory;
import org.agilewiki.jactor.concurrent.JAThreadManager;
import org.agilewiki.jactor.concurrent.ThreadManager;
import org.agilewiki.jactor.events.JAEventFuture;
import org.testng.annotations.Test;

import java.util.concurrent.ThreadFactory;

public class EchoTimingTest {
    @Test
    public void timing() {
        int c = 10;
        //int c = 10000000; //c should be at least 10 million
        ThreadFactory threadFactory = new JAThreadFactory();
        ThreadManager threadManager = new JAThreadManager();
        threadManager.start(1, threadFactory);
        try {
            Sender sender = new Sender(threadManager, c);
            long t0 = System.currentTimeMillis();
            JAEventFuture<Object> eventFuture = new JAEventFuture<Object>();
            eventFuture.send(sender, eventFuture);
            long t1 = System.currentTimeMillis();
            if (t1 != t0) {
                System.out.println(""+(2*c)+" messages sent");
                System.out.println("msgs per sec = " + (c * 2L * 1000L / (t1 - t0)));
            }
        } finally {
            threadManager.close();
        }
    }
}
