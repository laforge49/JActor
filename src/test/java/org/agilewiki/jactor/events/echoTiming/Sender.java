package org.agilewiki.jactor.events.echoTiming;

import org.agilewiki.jactor.concurrent.ThreadManager;
import org.agilewiki.jactor.events.JAEventActor;

import java.util.concurrent.Semaphore;

public class Sender extends JAEventActor<Object> {

    private ThreadManager threadManager;
    private Semaphore done = new Semaphore(0);
    private Echo echo = new Echo(threadManager);
    private int count = 0;
    private int i = 0;
    private long t0 = 0L;

    public Sender(ThreadManager threadManager) {
        super(threadManager);
        this.threadManager = threadManager;
    }

    public void finished() {
        try {
            done.acquire();
        } catch (InterruptedException e) {
        }
    }

    @Override
    public void processEvent(Object event) {
        if (event instanceof Integer) {
            int c = ((Integer) event).intValue();
            count = c;
            i = c;
            t0 = System.currentTimeMillis();
            send(echo, this);
        } else if (i > 0) {
            i -= 1;
            send(echo,this);
        } else {
            long t1 = System.currentTimeMillis();
            if (t1 != t0)
                System.out.println("msgs per sec = " + (count * 2L * 1000L / (t1 - t0)));
            threadManager.close();
            done.release();
        }

    }
}
