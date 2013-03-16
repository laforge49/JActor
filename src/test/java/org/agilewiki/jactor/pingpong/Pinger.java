package org.agilewiki.jactor.pingpong;

import org.agilewiki.jactor.Actor;
import org.agilewiki.jactor.JAFuture;
import org.agilewiki.jactor.Mailbox;
import org.agilewiki.jactor.RP;
import org.agilewiki.jactor.lpc.JLPCActor;
import org.agilewiki.jactor.lpc.Request;

/**
 * The Pinger's job is to hammer the Ponger with ping() request,
 * to count how many can be done in one second.
 */
public class Pinger extends JLPCActor {
    /* Hammer request result */
    public static final class HammerResult {
        /** Number of pings sent. */
        private final int pings;

        /** Duration. */
        private final double duration;

        /** Constructor */
        public HammerResult(final int _pings, final double _duration) {
            pings = _pings;
            duration = _duration;
        }

        /** toString */
        @Override
        public String toString() {
            return "Sent " + pings + " pings in " + duration + " seconds";
        }

        /** Number of pings sent. */
        public int pings() {
            return pings;
        }

        /** Duration. */
        public double duration() {
            return duration;
        }
    }

    /** How long to send pings? */
    private static final long PING_FOR_IN_MS = 1000L;

    /** The name of the pinger. */
    private final String name;

    /** A Hammer request, targeted at Pinger. */
    private static class HammerRequest extends Request<HammerResult,Pinger> {
        /** The Ponger to hammer. */
        private final Ponger ponger;

        /** Creates a hammer request, with the targeted Ponger. */
        public HammerRequest(final Ponger _ponger) {
            ponger = _ponger;
        }

        /** Process the hammer request. */
        @SuppressWarnings("unchecked")
        @Override
        public void processRequest(final JLPCActor targetActor,
                @SuppressWarnings("rawtypes") final RP responseProcessor)
                throws Exception {
            final Pinger pinger = (Pinger) targetActor;
            final String name = pinger.name;
            int count = 0;
            final long start = System.nanoTime();
            final long delay = PING_FOR_IN_MS * 1000000L;
            long now;
            while ((now = System.nanoTime()) - start < delay) {
                ponger.ping(name);
                count++;
            }
            responseProcessor.processResponse(new HammerResult(count,
                    ((now - start) / 1000000000.0d)));
        }

        @Override
        public boolean isTargetType(Actor targetActor) {
            return Pinger.class.isInstance(targetActor);
        }
    }

    /** Creates a Pinger, with it's own mailbox and name.
     * @throws Exception */
    public Pinger(final Mailbox mbox, final String _name) throws Exception {
        initialize(mbox);
        name = _name;
    }

    /** Tells the pinger to hammer the Ponger. Describes the speed in the result. */
    public HammerResult hammer(final Ponger ponger) throws Exception {
        final JAFuture future = new JAFuture();
        return new HammerRequest(ponger).send(future, this);
    }
}
