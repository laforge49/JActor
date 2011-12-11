package org.agilewiki.jactor.events.echoTiming;

import org.agilewiki.jactor.concurrent.ThreadManager;
import org.agilewiki.jactor.events.EventDestination;
import org.agilewiki.jactor.events.ActiveEventProcessor;
import org.agilewiki.jactor.events.JAEventQueue;

import java.util.concurrent.Semaphore;

public class Sender implements ActiveEventProcessor<Object>, EventDestination<Object> {

    private ThreadManager threadManager;
    private JAEventQueue<Object> eventQueue;
    private Semaphore done = new Semaphore(0);
    private Echo echo = new Echo(threadManager);
    private int count = 0;
    private int i = 0;
    private long t0 = 0L;

    public Sender(ThreadManager threadManager) {
        this.threadManager = threadManager;
        eventQueue = new JAEventQueue<Object>(threadManager);
        eventQueue.setEventProcessor(this);
    }

    public void finished() {
        try {
            done.acquire();
        } catch (InterruptedException e) {
        }
    }

    @Override
    public void putEvent(Object event) {
        eventQueue.putEvent(event);
    }

    @Override
    public void processEvent(Object event) {
        if (event instanceof Integer) {
            int c = ((Integer) event).intValue();
            count = c;
            i = c;
            t0 = System.currentTimeMillis();
            echo.putEvent(this);
        } else if (i > 0) {
            i -= 1;
            echo.putEvent(this);
        } else {
            long t1 = System.currentTimeMillis();
            if (t1 != t0)
                System.out.println("msgs per sec = " + (count * 2L * 1000L / (t1 - t0)));
            threadManager.close();
            done.release();
        }

    }

    @Override
    public void haveEvents() {
        eventQueue.dispatchEvents();
    }
}
