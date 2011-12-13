package org.agilewiki.jactor.bufferedEvents;

import org.agilewiki.jactor.concurrent.ThreadManager;

public class Driver extends JABufferedEventsActor<Object> {

    private Sender sender1;
    private Sender sender2;
    private BufferedEventsDestination<Object> source;
    private int pending;

    public Driver(ThreadManager threadManager, int c) {
        super(threadManager);
        sender1 = new Sender(threadManager, c);
        sender2 = new Sender(threadManager, c);
    }

    @Override
    protected void processEvent(Object event) {
        if (!(event instanceof Sender)) {
            source = (BufferedEventsDestination<Object>) event;
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
