package org.agilewiki.jactor.events.echoTiming;

import org.agilewiki.jactor.concurrent.JAThreadFactory;
import org.agilewiki.jactor.concurrent.JAThreadManager;
import org.agilewiki.jactor.concurrent.ThreadManager;
import org.testng.annotations.Test;

import java.util.concurrent.ThreadFactory;

public class EchoTimingTest {
    @Test
    public void timing() {
        ThreadFactory threadFactory = new JAThreadFactory();
        ThreadManager threadManager = new JAThreadManager();
        threadManager.start(1, threadFactory);
        Sender sender = new Sender(threadManager);
        int c = 10;
        //int c = 10000000; //c should be at least 10 million
        sender.put(c);
        sender.finished();
    }
}
