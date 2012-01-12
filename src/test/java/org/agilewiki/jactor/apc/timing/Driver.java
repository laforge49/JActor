package org.agilewiki.jactor.apc.timing;

import org.agilewiki.jactor.ResponseProcessor;
import org.agilewiki.jactor.apc.JAPCActor;
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
    public void processRequest(final Object unwrappedRequest, final ResponseProcessor rd1) throws Exception {
        ResponseProcessor rd2 = new ResponseProcessor() {
            int r;

            @Override
            public void process(Object unwrappedResponse) throws Exception {
                r += 1;
                if (r == p) rd1.process(null);
            }
        };
        int i = 0;
        while (i < p) {
            send(senders[i], null, rd2);
            i += 1;
        }
    }

    /**
     * Returns true when the concurrent data of the actor, or its parent, contains the named data item.
     *
     * @param name The key for the data item.
     * @return True when the concurrent data of the actor, or its parent, contains the named data item.
     */
    @Override
    public boolean hasDataItem(String name) {
        return false;
    }
}
