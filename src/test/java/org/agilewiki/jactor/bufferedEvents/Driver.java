package org.agilewiki.jactor.bufferedEvents;

import org.agilewiki.jactor.concurrent.ThreadManager;

final public class Driver extends JABufferedEventsActor<Object> {

    private Sender[] senders;
    private BufferedEventsDestination<Object> source;
    private int pending;
    private int p;

    public Driver(ThreadManager threadManager, int c, int p) {
        super(threadManager);
        this.p = p;
        int i = 0;
        while (i < p) {
            senders[i] = new Sender(threadManager, c);
            i += 1;
        }
    }

    @Override
    protected void processEvent(Object event) {
        if (!(event instanceof Sender)) {
            source = (BufferedEventsDestination<Object>) event;
            pending = p;
            int i = 0;
            while (i < p) {
                send(senders[i], this);
                i += 1;
            }
        } else {
            pending -= 1;
            if (pending == 0)
                send(source, this);
        }
    }
}
