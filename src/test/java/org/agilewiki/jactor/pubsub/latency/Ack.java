package org.agilewiki.jactor.pubsub.latency;

import org.agilewiki.jactor.Actor;
import org.agilewiki.jactor.lpc.Request;

/**
 * Test code.
 */
public class Ack extends Request<Object, Src> {
    public final static Ack req = new Ack();

    @Override
    public boolean isTargetType(Actor targetActor) {
        return targetActor instanceof Src;
    }
}
