package org.agilewiki.jactor.bufferedEvents;

import org.agilewiki.jactor.concurrent.ThreadManager;

public class Sender extends JABufferedEventsActor<Object> {

    private Echo echo;
    private int count = 0;
    private int i = 0;
    private BufferedEventsDestination<Object> source;

    public Sender(ThreadManager threadManager, int c) {
        super(threadManager);
        echo = new Echo(threadManager);
        count = c;
    }

    @Override
    protected void processEvent(Object event) {
        if (!(event instanceof Echo)) {
            source = (BufferedEventsDestination<Object>) event;
            i = count;
            send(echo, this);
        } else if (i > 0) {
            i -= 1;
            send(echo, this);
        } else {
            send(source, this);
        }
    }
}
