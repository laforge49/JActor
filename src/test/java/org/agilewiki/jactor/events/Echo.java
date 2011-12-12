package org.agilewiki.jactor.events;

import org.agilewiki.jactor.concurrent.ThreadManager;

public final class Echo extends JAEventActor<Object> {

    public Echo(ThreadManager threadManager) {
        super(threadManager);
    }

    @Override
    public void processEvent(Object event) {
        EventDestination<Object> destination = (EventDestination<Object>) event;
        send(destination, this);
    }
}
