package org.agilewiki.jactor.components.pubsub;

import org.agilewiki.jactor.Mailbox;
import org.agilewiki.jactor.RP;
import org.agilewiki.jactor.bind.Internals;
import org.agilewiki.jactor.bind.JBActor;
import org.agilewiki.jactor.bind.MethodBinding;

/**
 * Test code.
 */
public class Subscriber extends JBActor {
    public Subscriber(Mailbox mailbox) {
        super(mailbox);

        bind(PSRequest.class.getName(), new MethodBinding<PSRequest, Object>() {
            @Override
            public void processRequest(Internals internals, PSRequest request, RP<Object> rp) throws Exception {
                rp.processResponse(null);
            }
        });
    }
}
