package org.agilewiki.jactor.factory;

import org.agilewiki.jactor.Mailbox;
import org.agilewiki.jactor.RP;
import org.agilewiki.jactor.lpc.JLPCActor;

/**
 * Test code.
 */
public class Foo extends JLPCActor {
    /**
     * Create a LiteActor
     *
     * @param mailbox A mailbox which may be shared with other actors.
     */
    public Foo(final Mailbox mailbox) {
        super(mailbox);
    }

    public void hi() {
        System.out.println("Hello world!");
    }

    /**
     * The application method for processing requests sent to the actor.
     *
     * @param request A request.
     * @param rp      The response processor.
     * @throws Exception Any uncaught exceptions raised while processing the request.
     */
    @Override
    protected void processRequest(Object request, RP rp) throws Exception {
        hi();
        rp.processResponse(null);
    }
}
