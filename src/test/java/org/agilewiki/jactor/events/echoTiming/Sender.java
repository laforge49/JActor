package org.agilewiki.jactor.events.echoTiming;

import org.agilewiki.jactor.concurrent.ThreadManager;
import org.agilewiki.jactor.events.JAEventActor;
import org.agilewiki.jactor.events.JAEventFuture;

public class Sender extends JAEventActor<Object> {

    private Echo echo;
    private int count = 0;
    private int i = 0;
    private JAEventFuture<Object> eventFuture;

    public Sender(ThreadManager threadManager, int c) {
        super(threadManager);
        echo = new Echo(threadManager);
        count = c;
    }

    @Override
    public void processEvent(Object event) {
        if (event instanceof JAEventFuture) {
            eventFuture = (JAEventFuture<Object>) event;
            i = count;
            send(echo, this);
        } else if (i > 0) {
            i -= 1;
            send(echo, this);
        } else {
            send(eventFuture, this);
        }

    }
}
