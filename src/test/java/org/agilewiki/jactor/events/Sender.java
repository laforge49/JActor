package org.agilewiki.jactor.events;

import org.agilewiki.jactor.concurrent.ThreadManager;

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
    protected void processEvent(Object event) {
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
