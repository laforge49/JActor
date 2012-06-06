package org.agilewiki.jactor.pubsub.latency;

import org.agilewiki.jactor.RP;
import org.agilewiki.jactor.pubsub.subscriber.JASubscriber;

/**
 * Test code.
 */
public class Sub extends JASubscriber {
    public Src src;
    private int count;

    @Override
    protected void processRequest(Object request, RP rp) throws Exception {
        if (request.getClass() == Ping.class) {
            rp.processResponse(null);
            return;
        }

        super.processRequest(request, rp);
    }
}
