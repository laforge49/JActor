package org.agilewiki.jactor.events.echoTiming;

import org.agilewiki.jactor.concurrent.ThreadManager;
import org.agilewiki.jactor.events.EventDispatcher;
import org.agilewiki.jactor.events.EventProcessor;
import org.agilewiki.jactor.events.JAEventDispatcher;

import java.util.concurrent.Semaphore;

public class Sender implements EventProcessor<Object> {

    private ThreadManager threadManager;
    private EventDispatcher<Object> eventDispatcher;
    private Semaphore done = new Semaphore(0);
    private Echo echo = new Echo(threadManager);
    private int count = 0;
    private int i = 0;
    private long t0 = 0L;

    public Sender(ThreadManager threadManager) {
        this.threadManager = threadManager;
        eventDispatcher = new JAEventDispatcher<Object>(threadManager);
        eventDispatcher.setEventProcessor(this);
    }
    
    public void finished() {
        try {
            done.acquire();
        } catch (InterruptedException e) {}
    }

    public void put(Object event) {
        eventDispatcher.put(event);
    }

    @Override
    public void processEvent(Object event) {
        if (event instanceof Integer) {
            int c = ((Integer) event).intValue();
            count = c;
            i = c;
            t0 = System.currentTimeMillis();
            echo.put(this);
        } else if (i > 0) {
            i -= 1;
            echo.put(this);
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
        eventDispatcher.dispatchEvents();
    }
}
