package org.agilewiki.jactor.lpc.timing;

import org.agilewiki.jactor.Actor;
import org.agilewiki.jactor.JAIterator;
import org.agilewiki.jactor.Mailbox;
import org.agilewiki.jactor.RP;
import org.agilewiki.jactor.lpc.JLPCActor;

public class Sender1 extends JLPCActor {

    private Actor echo;
    private final int count;

    public Sender1(Mailbox mailbox, Actor echo, int c, int b) {
        super(mailbox);
        this.echo = echo;
        echo.setInitialBufferCapacity(b + 10);
        count = c;
    }

    @Override
    public void processRequest(final Object unwrappedRequest, final RP rd1)
            throws Exception {
        (new JAIterator() {
            int i;

            @Override
            public void process(final RP rd2) throws Exception {
                if (i > count) rd2.process(this);
                else {
                    i += 1;
                    send(echo, null, rd2);
                }
            }
        }).iterate(rd1);
    }
}
