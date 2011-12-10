package org.agilewiki.jactor.events.echoTiming;

import org.agilewiki.jactor.concurrent.ThreadManager;
import org.agilewiki.jactor.events.EventDestination;
import org.agilewiki.jactor.events.EventDispatcher;
import org.agilewiki.jactor.events.EventProcessor;
import org.agilewiki.jactor.events.JAEventDispatcher;

public final class Echo implements EventProcessor<Object>, EventDestination<Object> {

    private EventDispatcher<Object> eventDispatcher;

    public Echo(ThreadManager threadManager) {
        eventDispatcher = new JAEventDispatcher<Object>(threadManager);
        eventDispatcher.setEventProcessor(this);
    }

    @Override
    public void putEvent(Object event) {
        eventDispatcher.putEvent(event);
    }

    @Override
    public void processEvent(Object event) {
        if (event instanceof Sender)
            ((Sender) event).putEvent(this);
    }

    @Override
    public void haveEvents() {
        eventDispatcher.dispatchEvents();
    }
}
