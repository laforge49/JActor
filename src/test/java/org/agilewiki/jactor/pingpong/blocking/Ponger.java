package org.agilewiki.jactor.pingpong.blocking;

import org.agilewiki.jactor.Actor;
import org.agilewiki.jactor.JAFuture;
import org.agilewiki.jactor.Mailbox;
import org.agilewiki.jactor.RP;
import org.agilewiki.jactor.lpc.JLPCActor;
import org.agilewiki.jactor.lpc.Request;

/**
 * Receives Pings, and send Pongs back.
 */
public class Ponger extends JLPCActor {
    /** Some mutable data of Ponger, which must be access in a thread-safe way. */
    private int pings;

    /** A Ping request, targeted at Ponger. */
    private static class PingRequest extends Request<String,Ponger> {
        private final String from;

        public PingRequest(final String _from) {
            from = _from;
        }

        /** Processes the ping(String) request, from within the Thread of the Ponger. */
        @SuppressWarnings("unchecked")
        @Override
        public void processRequest(final JLPCActor targetActor,
                @SuppressWarnings("rawtypes") final RP responseProcessor)
                throws Exception {
            final Ponger ponger = (Ponger) targetActor;
            responseProcessor.processResponse("Pong " + (ponger.pings++) + " to "
                    + from + "!");
        }

        @Override
        public boolean isTargetType(Actor targetActor) {
            return Ponger.class.isInstance(targetActor);
        }
    }

    /** Creates a Ponger, with it's own mailbox.
     * @throws Exception */
    public Ponger(final Mailbox mbox) throws Exception {
        initialize(mbox);
    }

    /** Sends a ping(String) request to the Ponger. Blocks and returns response. */
    public String ping(final String from) throws Exception {
        final JAFuture future = new JAFuture();
        return new PingRequest(from).send(future, this);
    }
}
