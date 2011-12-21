package org.agilewiki.jactor.lpc.timing;

import org.agilewiki.jactor.apc.Function;
import org.agilewiki.jactor.apc.JAIterator;
import org.agilewiki.jactor.apc.ResponseProcessor;
import org.agilewiki.jactor.lpc.JLPCActor;
import org.agilewiki.jactor.lpc.LPCActor;
import org.agilewiki.jactor.lpc.LPCMailbox;

public class Sender1 extends JLPCActor {

    private LPCActor echo;
    private final int count;

    public Sender1(LPCMailbox mailbox, LPCActor echo, int c, int b) {
        super(mailbox);
        this.echo = echo;
        echo.setInitialBufferCapacity(b + 10);
        count = c;
    }

    @Override
    protected void processRequest(final Object unwrappedRequest, final ResponseProcessor rd1)
            throws Exception {
        JAIterator.iterate(new Function() {
            int i = 0;

            @Override
            public void process(final ResponseProcessor rd2) throws Exception {
                if (i > count) rd2.process(this);
                else {
                    i += 1;
                    send(echo, null, rd2);
                }
            }

        }, rd1);
    }
}
