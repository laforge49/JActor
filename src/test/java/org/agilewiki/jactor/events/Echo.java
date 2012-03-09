package org.agilewiki.jactor.events;

import org.agilewiki.jactor.concurrent.ThreadManager;

/**
 * Test code.
 */
public final class Echo extends JAEventActor<Object> {

    public Echo(ThreadManager threadManager) {
        super(threadManager);
    }

    @Override
    protected void processEvent(Object event) {
        EventDestination<Object> destination = (EventDestination<Object>) event;
        send(destination, this);
    }
}
