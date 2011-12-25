package org.agilewiki.jactor.apc.timing;

import org.agilewiki.jactor.JAIterator;
import org.agilewiki.jactor.apc.JAPCActor;
import org.agilewiki.jactor.apc.ResponseProcessor;
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
    protected void processRequest(final Object unwrappedRequest, final ResponseProcessor rd1) throws Exception {
        (new JAIterator(rd1) {
            int i;

            @Override
            public void process(final ResponseProcessor rd2) throws Exception {
                if (i > count) rd2.process(this);
                else {
                    i += 1;
                    ResponseProcessor rd3 = new ResponseProcessor() {
                        int r = burst;

                        @Override
                        public void process(Object unwrappedResponse) throws Exception {
                            r -= 1;
                            if (r == 0) rd2.process(null);
                        }
                    };
                    int j = 0;
                    while (j < burst) {
                        send(echo, null, rd3);
                        j += 1;
                    }
                }
            }
        }).iterate();
    }
}
