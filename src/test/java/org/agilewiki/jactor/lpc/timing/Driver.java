package org.agilewiki.jactor.lpc.timing;

import org.agilewiki.jactor.Actor;
import org.agilewiki.jactor.Mailbox;
import org.agilewiki.jactor.apc.ResponseProcessor;
import org.agilewiki.jactor.lpc.JLPCActor;

public class Driver extends JLPCActor {
    private int p;
    private Actor[] senders;

    public Driver(Mailbox mailbox, Actor[] senders, int p) {
        super(mailbox);
        this.senders = senders;
        this.p = p;
    }

    @Override
    protected void processRequest(final Object unwrappedRequest, final ResponseProcessor rd1) throws Exception {
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
}
