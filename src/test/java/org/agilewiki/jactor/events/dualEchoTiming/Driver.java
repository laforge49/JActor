package org.agilewiki.jactor.events.dualEchoTiming;

import org.agilewiki.jactor.concurrent.ThreadManager;
import org.agilewiki.jactor.events.EventDestination;
import org.agilewiki.jactor.events.JAEventActor;
import org.agilewiki.jactor.events.Sender;

public final class Driver extends JAEventActor<Object> {

    private Sender sender1;
    private Sender sender2;
    private EventDestination<Object> source;
    private int pending;

    public Driver(ThreadManager threadManager, int c) {
        super(threadManager);
        sender1 = new Sender(threadManager, c);
        sender2 = new Sender(threadManager, c);
    }

    @Override
    protected void processEvent(Object event) {
        if (!(event instanceof Sender)) {
            source = (EventDestination<Object>) event;
            pending = 2;
            send(sender1, this);
            send(sender2, this);
        } else {
            pending -= 1;
            if (pending == 0)
                send(source, this);
        }
    }
}
