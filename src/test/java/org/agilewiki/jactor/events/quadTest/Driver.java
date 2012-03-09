package org.agilewiki.jactor.events.quadTest;

import org.agilewiki.jactor.concurrent.ThreadManager;
import org.agilewiki.jactor.events.Echo;
import org.agilewiki.jactor.events.EventDestination;
import org.agilewiki.jactor.events.JAEventActor;

/**
 * Test code.
 */
public final class Driver extends JAEventActor<Object> {

    private Sender sender1;
    private Sender sender2;
    private Sender sender3;
    private Sender sender4;
    private EventDestination<Object> source;
    private int pending;

    public Driver(ThreadManager threadManager, int c, Echo echo) {
        super(threadManager);
        sender1 = new Sender(threadManager, c, echo);
        sender2 = new Sender(threadManager, c, echo);
        sender3 = new Sender(threadManager, c, echo);
        sender4 = new Sender(threadManager, c, echo);
    }

    @Override
    protected void processEvent(Object event) {
        if (!(event instanceof Sender)) {
            source = (EventDestination<Object>) event;
            pending = 4;
            send(sender1, this);
            send(sender2, this);
            send(sender3, this);
            send(sender4, this);
        } else {
            pending -= 1;
            if (pending == 0)
                send(source, this);
        }
    }
}
