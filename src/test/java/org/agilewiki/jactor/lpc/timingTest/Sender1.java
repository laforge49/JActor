package org.agilewiki.jactor.lpc.timingTest;

import org.agilewiki.jactor.*;
import org.agilewiki.jactor.lpc.JLPCActor;

/**
 * Test code.
 */
public class Sender1 extends JLPCActor implements SimpleRequestReceiver, RealRequestReceiver {

    private Actor echo;
    private final int count;

    public Sender1(Actor echo, int c, int b) {
        this.echo = echo;
        echo.setInitialBufferCapacity(b + 10);
        count = c;
    }

    @Override
    public void processRequest(final SimpleRequest unwrappedRequest, final RP rd1)
            throws Exception {
        (new JAIterator() {
            int i;

            @Override
            public void process(final RP rd2) throws Exception {
                if (i > count) rd2.processResponse(this);
                else {
                    i += 1;
                    rd2.processResponse(null);
                }
            }
        }).iterate(rd1);
    }

    @Override
    public void processRequest(final RealRequest unwrappedRequest, final RP rd1)
            throws Exception {
        (new JAIterator() {
            int i;

            @Override
            public void process(final RP rd2) throws Exception {
                if (i > count) rd2.processResponse(this);
                else {
                    i += 1;
                    SimpleRequest.req.send(Sender1.this, echo, rd2);
                }
            }
        }).iterate(rd1);
    }
}
