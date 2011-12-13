package org.agilewiki.jactor.bufferedEvents;

import org.agilewiki.jactor.concurrent.ThreadManager;

public final class Echo extends JABufferedEventsActor<Object> {

    public Echo(ThreadManager threadManager) {
        super(threadManager);
    }

    @Override
    protected void processEvent(Object event) {
        BufferedEventsDestination<Object> destination = (BufferedEventsDestination<Object>) event;
        send(destination, this);
    }
}
