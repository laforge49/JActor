package org.agilewiki.jactor.events.quadTest;

import org.agilewiki.jactor.concurrent.ThreadManager;
import org.agilewiki.jactor.events.Echo;
import org.agilewiki.jactor.events.EventDestination;
import org.agilewiki.jactor.events.JAEventActor;

/**
 * Test code.
 */
public class Sender extends JAEventActor<Object> {

    private Echo echo;
    private int count = 0;
    private int i = 0;
    private EventDestination<Object> source;

    public Sender(ThreadManager threadManager, int c, Echo echo) {
        super(threadManager);
        this.echo = echo;
        count = c;
    }

    @Override
    protected void processEvent(Object event) {
        if (!(event instanceof Echo)) {
            source = (EventDestination<Object>) event;
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
