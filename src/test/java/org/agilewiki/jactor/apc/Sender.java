package org.agilewiki.jactor.apc;

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
    protected void processRequest(final Object data, final ResponseDestination rd1) {
        iterate(new APCFunction() {
            int i = 0;

            @Override
            public void process(final ResponseDestination rd2) {
                if (i > count) rd2.process(this);
                else {
                    i += 1;
                    ResponseDestination rd3 = new ResponseDestination() {
                        int r = burst;

                        @Override
                        public void process(Object result) {
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
        }, rd1);
    }
}
