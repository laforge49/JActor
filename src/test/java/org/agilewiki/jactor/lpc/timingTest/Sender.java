package org.agilewiki.jactor.lpc.timingTest;

import org.agilewiki.jactor.*;
import org.agilewiki.jactor.lpc.JLPCActor;

/**
 * Test code.
 */
public class Sender extends JLPCActor implements SimpleRequestReceiver, RealRequestReceiver {

    private Actor echo;
    private final int count;
    private final int burst;

    public Sender(Actor echo, int c, int b) {
        this.echo = echo;
        echo.setInitialBufferCapacity(b + 10);
        count = c;
        burst = b;
    }

    @Override
    public void processRequest(final SimpleRequest unwrappedRequest, final RP rd1) throws Exception {
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
                        rd3.processResponse(null);
                        j += 1;
                    }
                }
            }
        }).iterate(rd1);
    }

    @Override
    public void processRequest(final RealRequest unwrappedRequest, final RP rd1) throws Exception {
        final boolean real = unwrappedRequest != null;
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
                        SimpleRequest.req.send(Sender.this, echo, rd3);
                        j += 1;
                    }
                }
            }
        }).iterate(rd1);
    }
}
