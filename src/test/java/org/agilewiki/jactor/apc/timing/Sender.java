package org.agilewiki.jactor.apc.timing;

import org.agilewiki.jactor.JAIterator;
import org.agilewiki.jactor.RP;
import org.agilewiki.jactor.apc.JAPCActor;
import org.agilewiki.jactor.concurrent.ThreadManager;

final public class Sender extends JAPCActor {

    private Echo echo;
    private final int count;
    private final int burst;

    public Sender(ThreadManager threadManager, int c, int b) {
        super(threadManager);
        echo = new Echo(threadManager);
        echo.setInitialBufferCapacity(b + 10);
        count = c;
        burst = b;
    }

    @Override
    public void processRequest(final Object unwrappedRequest, final RP rd1) throws Exception {
        (new JAIterator() {
            int i;

            @Override
            public void process(final RP rd2) throws Exception {
                if (i > count) rd2.processResponse(this);
                else {
                    i += 1;
                    RP rd3 = new RP() {
                        int r = burst;

                        @Override
                        public void processResponse(Object unwrappedResponse) throws Exception {
                            r -= 1;
                            if (r == 0) rd2.processResponse(null);
                        }
                    };
                    int j = 0;
                    while (j < burst) {
                        send(echo, null, rd3);
                        j += 1;
                    }
                }
            }
        }).iterate(rd1);
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
