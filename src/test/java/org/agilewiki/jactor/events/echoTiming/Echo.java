package org.agilewiki.jactor.events.echoTiming;

import org.agilewiki.jactor.concurrent.ThreadManager;
import org.agilewiki.jactor.events.EventDispatcher;
import org.agilewiki.jactor.events.EventProcessor;
import org.agilewiki.jactor.events.JAEventDispatcher;

public final class Echo implements EventProcessor<Object> {

    private EventDispatcher<Object> eventDispatcher;

    public Echo(ThreadManager threadManager) {
        eventDispatcher = new JAEventDispatcher<Object>(threadManager);
        eventDispatcher.setEventProcessor(this);
    }

    public void put(Object event) {
        eventDispatcher.put(event);
    }

    @Override
    public void processEvent(Object event) {
        if (event instanceof Sender)
            ((Sender) event).put(this);
    }

    @Override
    public void haveEvents() {
        eventDispatcher.dispatchEvents();
    }
}
