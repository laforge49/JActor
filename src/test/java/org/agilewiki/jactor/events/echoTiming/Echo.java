package org.agilewiki.jactor.events.echoTiming;

import org.agilewiki.jactor.concurrent.ThreadManager;
import org.agilewiki.jactor.events.EventDestination;
import org.agilewiki.jactor.events.JAEventActor;

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
