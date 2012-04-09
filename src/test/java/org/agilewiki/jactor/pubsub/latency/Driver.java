package org.agilewiki.jactor.pubsub.latency;

import org.agilewiki.jactor.Mailbox;
import org.agilewiki.jactor.RP;
import org.agilewiki.jactor.lpc.JLPCActor;
import org.agilewiki.jactor.pubsub.actorName.SetActorName;
import org.agilewiki.jactor.pubsub.publisher.JAPublisher;
import org.agilewiki.jactor.pubsub.publisher.Publish;
import org.agilewiki.jactor.pubsub.publisher.Subscribe;

/**
 * Test code.
 */
public class Driver extends JLPCActor implements Src {
    public int r;
    public int s;
    public int m;
    public JAPublisher pub;
    private RP done;
    private int count;
    private Publish publish = new Publish(Ping.req);

    public Driver(final Mailbox mailbox) {
        super(mailbox);
    }

    private void ack() throws Exception {
        if (count == r * s) {
            done.processResponse(null);
            return;
        }
        if (count % s == 0) {
            int i = 0;
            while (i < m) {
                publish.sendEvent(pub);
                i += 1;
            }
        }
        count += 1;
    }

    @Override
    protected void processRequest(Object request, RP rp) throws Exception {
        Class reqcls = request.getClass();

        if (reqcls == Go.class) {
            done = rp;
            count = 0;
            int i = 0;
            while (i < s) {
                Sub sub = new Sub(getMailbox());
                sub.setInitialBufferCapacity(1000);
                SetActorName san = new SetActorName("" + i);
                san.sendEvent(sub);
                sub.mod = m;
                sub.src = this;
                Subscribe subscribe = new Subscribe(sub);
                subscribe.sendEvent(pub);
                i += 1;
            }
            ack();
            return;
        }

        if (reqcls == Ack.class) {
            ack();
            rp.processResponse(null);
            return;
        }

        throw new UnsupportedOperationException(reqcls.getName());
    }
}
