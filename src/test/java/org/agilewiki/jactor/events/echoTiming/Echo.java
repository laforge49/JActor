package org.agilewiki.jactor.events.echoTiming;

import org.agilewiki.jactor.concurrent.ThreadManager;
import org.agilewiki.jactor.events.EventDestination;
import org.agilewiki.jactor.events.EventDispatcher;
import org.agilewiki.jactor.events.ActiveEventProcessor;
import org.agilewiki.jactor.events.JAEventQueue;

public final class Echo implements ActiveEventProcessor<Object>, EventDestination<Object> {

    private JAEventQueue<Object> eventQueue;

    public Echo(ThreadManager threadManager) {
        eventQueue = new JAEventQueue<Object>(threadManager);
        eventQueue.setEventProcessor(this);
    }

    @Override
    public void putEvent(Object event) {
        eventQueue.putEvent(event);
    }

    @Override
    public void processEvent(Object event) {
        if (event instanceof Sender)
            ((Sender) event).putEvent(this);
    }

    @Override
    public void haveEvents() {
        eventQueue.dispatchEvents();
    }
}
