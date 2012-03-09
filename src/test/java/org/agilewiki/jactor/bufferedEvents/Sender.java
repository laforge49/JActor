package org.agilewiki.jactor.bufferedEvents;

import org.agilewiki.jactor.concurrent.ThreadManager;

/**
 * Test code.
 */
public class Sender extends JABufferedEventsActor<Object> {

    private Echo echo;
    private int count = 0;
    private int i = 0;
    private int burst = 0;
    private int j = 0;
    private int r = 0;
    private BufferedEventsDestination<Object> source;

    public Sender(ThreadManager threadManager, int c, int b) {
        super(threadManager);
        echo = new Echo(threadManager);
        echo.setInitialBufferCapacity(b + 10);
        count = c;
        burst = b;
    }

    @Override
    protected void processEvent(Object event) {
        if (!(event instanceof Echo)) {
            source = (BufferedEventsDestination<Object>) event;
            i = count;
            j = 0;
            r = 0;
            send(echo, this);
        } else if (r > 1) {
            r -= 1;
        } else if (i > 0) {
            i -= 1;
            j = burst;
            r = burst;
            while (j > 0) {
                j -= 1;
                send(echo, this);
            }
        } else {
            send(source, this);
        }
    }
}
