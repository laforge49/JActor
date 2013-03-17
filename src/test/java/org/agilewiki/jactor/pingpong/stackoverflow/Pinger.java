package org.agilewiki.jactor.pingpong.stackoverflow;

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
    public static final class HammerResult2 {
        /** Number of pings sent. */
        private final int pings;

        /** Duration. */
        private final double duration;

        /** Constructor */
        public HammerResult2(final int _pings, final double _duration) {
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
    private static class HammerRequest2 extends Request<HammerResult2,Pinger> {
        private static final long DELAY = PING_FOR_IN_MS * 1000000L;

        /** The Ponger to hammer. */
        private final Ponger ponger;

        /** The responseProcessor from the test, to call when done. */
        private RP<HammerResult2> responseProcessor;

        /** Arival from hammer request. */
        private long start;

        /** The number of pings. */
        private int count;

        /** The pinger */
        private Pinger pinger;

        /** Creates a hammer request, with the targeted Ponger. */
        public HammerRequest2(final Ponger _ponger) {
            ponger = _ponger;
        }

        private void ping() throws Exception {
            ponger.ping(pinger, new RP<String>() {
                @Override
                public void processResponse(String response)
                        throws Exception {
                    final long now = System.nanoTime();
                    count++;
                    if (now  - start < DELAY) {
                        // again ...
                        ping();
                    } else {
                        responseProcessor.processResponse(new HammerResult2(count,
                            ((now - start) / 1000000000.0d)));
                    }

                }
            });
        }

        /** Process the hammer request. */
        @SuppressWarnings("unchecked")
        @Override
        public void processRequest(final JLPCActor targetActor,
                @SuppressWarnings("rawtypes") final RP _responseProcessor)
                throws Exception {
            pinger = (Pinger) targetActor;
            responseProcessor = (RP<HammerResult2>) _responseProcessor;
            start = System.nanoTime();
            boolean again = true;
            while (again) {
                try {
                    ping();
                    again = false;
                } catch (StackOverflowError e) {
                    // NOP
                }
            }
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
    public HammerResult2 hammer(final Ponger ponger) throws Exception {
        final JAFuture future = new JAFuture();
        return new HammerRequest2(ponger).send(future, this);
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }
}
