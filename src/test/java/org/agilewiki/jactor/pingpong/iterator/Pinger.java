package org.agilewiki.jactor.pingpong.iterator;

import org.agilewiki.jactor.Actor;
import org.agilewiki.jactor.JABidiIterator;
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
    public static final class HammerResult3 {
        /** Number of pings sent. */
        private final int pings;

        /** Duration. */
        private final double duration;

        /** Constructor */
        public HammerResult3(final int _pings, final double _duration) {
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
    private static class HammerRequest3 extends Request<HammerResult3,Pinger> {
        private static final long DELAY = PING_FOR_IN_MS * 1000000L;

        /** The Ponger to hammer. */
        private final Ponger ponger;

        /** Arival from hammer request. */
        private long start;

        /** The number of pings. */
        private int count;

        /** The pinger */
        private Pinger pinger;

        /** Creates a hammer request, with the targeted Ponger. */
        public HammerRequest3(final Ponger _ponger) {
            ponger = _ponger;
        }

        /** Process the hammer request. */
        @SuppressWarnings("unchecked")
        @Override
        public void processRequest(final JLPCActor targetActor,
                @SuppressWarnings("rawtypes") final RP _responseProcessor)
                throws Exception {
            pinger = (Pinger) targetActor;
            start = System.nanoTime();

            (new JABidiIterator() {
                @Override
                protected void sendRequest(@SuppressWarnings("rawtypes") RP responseProcessor) throws Exception {
                    ponger.ping(pinger, responseProcessor);
                }

                @Override
                protected Object processResponse(Object response) throws Exception {
                    // response is ignored in this case, but we *could* have used it,
                    // which is the main thing.
                    final long now = System.nanoTime();
                    count++;
                    if (now  - start < DELAY) {
                        return null;
                    } else {
                        return new HammerResult3(count, ((now - start) / 1000000000.0d));
                    }
                }
            }).iterate(_responseProcessor);
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
    public HammerResult3 hammer(final Ponger ponger) throws Exception {
        final JAFuture future = new JAFuture();
        return new HammerRequest3(ponger).send(future, this);
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }
}
