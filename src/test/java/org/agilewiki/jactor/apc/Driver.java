package org.agilewiki.jactor.apc;

import org.agilewiki.jactor.concurrent.ThreadManager;

final public class Driver extends JAPCActor {
    private int p;
    private Sender[] senders;

    public Driver(ThreadManager threadManager, int c, int b, int p) {
        super(threadManager);
        this.p = p;
        int i = 0;
        senders = new Sender[p];
        while (i < p) {
            senders[i] = new Sender(threadManager, c, b);
            senders[i].setInitialBufferCapacity(b + 10);
            i += 1;
        }
    }

    @Override
    protected void processRequest(final Object data, final ResponseDestination rd1) {
        ResponseDestination rd2 = new ResponseDestination() {
            int r = p;
            
            @Override
            public void process(Object result) {
                r -= 1;
                if (r == 0) rd1.process(null);
            }
        };
        int i = 0;
        while (i < p) {
            send(senders[i], null, rd2);
            i += 1;
        }
    }
}
