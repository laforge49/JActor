package org.agilewiki.jactor.pubsub.latency;

import org.agilewiki.jactor.Mailbox;
import org.agilewiki.jactor.RP;
import org.agilewiki.jactor.pubsub.subscriber.JASubscriber;

/**
 * Test code.
 */
public class Sub extends JASubscriber {
    public int mod;
    public Src src;
    private int count;

    public Sub(Mailbox mailbox) {
        super(mailbox);
    }

    public void ping()
            throws Exception {
        count += 1;
        if (count % mod == 0) {
            Ack.req.sendEvent(src);
        }
    }

    @Override
    protected void processRequest(Object request, RP rp) throws Exception {
        if (request.getClass() == Ping.class) {
            ping();
            rp.processResponse(null);
            return;
        }

        super.processRequest(request, rp);
    }
}
