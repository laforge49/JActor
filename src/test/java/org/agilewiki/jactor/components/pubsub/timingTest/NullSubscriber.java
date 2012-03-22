package org.agilewiki.jactor.components.pubsub.timingTest;

import org.agilewiki.jactor.Mailbox;
import org.agilewiki.jactor.RP;
import org.agilewiki.jactor.bind.Internals;
import org.agilewiki.jactor.bind.JBActor;
import org.agilewiki.jactor.bind.MethodBinding;

/**
 * Test code.
 */
final public class NullSubscriber extends JBActor {
    public NullSubscriber(Mailbox mailbox) {
        super(mailbox);

        bind(NullRequest.class.getName(), new MethodBinding<NullRequest, Object>() {
            @Override
            public void processRequest(Internals internals, NullRequest request, RP<Object> rp)
                    throws Exception {
                rp.processResponse(null);
            }
        });
    }
}
