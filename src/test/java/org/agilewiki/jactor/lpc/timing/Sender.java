package org.agilewiki.jactor.lpc.timing;

import org.agilewiki.jactor.Actor;
import org.agilewiki.jactor.JAIterator;
import org.agilewiki.jactor.Mailbox;
import org.agilewiki.jactor.RP;
import org.agilewiki.jactor.lpc.JLPCActor;

public class Sender extends JLPCActor {

    private Actor echo;
    private final int count;
    private final int burst;

    public Sender(Mailbox mailbox, Actor echo, int c, int b) {
        super(mailbox);
        this.echo = echo;
        echo.setInitialBufferCapacity(b + 10);
        count = c;
        burst = b;
    }

    @Override
    public void processRequest(final Object unwrappedRequest, final RP rd1) throws Exception {
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
                        if (real)
                            send(echo, null, rd3);
                        else
                            rd3.processResponse(null);
                        j += 1;
                    }
                }
            }
        }).iterate(rd1);
    }
}
