package org.agilewiki.jactor.pubsub.timing;

import org.agilewiki.jactor.*;
import org.agilewiki.jactor.lpc.JLPCActor;
import org.agilewiki.jactor.pubsub.PubSub;
import org.agilewiki.jactor.pubsub.Publish;

final public class Driver1 extends JLPCActor {
    public Actor pubsub;

    public Driver1(Mailbox mailbox) {
        super(mailbox);
        pubsub = new PubSub(mailbox);
    }

    @Override
    protected void processRequest(Object request, ResponseProcessor rp) throws Exception {
        Timing timing = (Timing) request;
        final int count = timing.getCount();
        JAIterator jaIterator = new JAIterator() {
            int i = 0;
            Publish publish = new Publish(new NullRequest());

            @Override
            protected void process(final ResponseProcessor rp1) throws Exception {
                if (i == count) {
                    rp1.process(JANull.jan);
                    return;
                }
                i += 1;
                send(pubsub, publish, new ResponseProcessor() {
                    @Override
                    public void process(Object response) throws Exception {
                        rp1.process(null);
                    }
                });
            }
        };
        jaIterator.iterate(rp);
    }
}
