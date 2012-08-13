package org.agilewiki.jactor.pubsub.latency;

import org.agilewiki.jactor.RP;
import org.agilewiki.jactor.lpc.TargetActor;

/**
 * Test code.
 */
public interface Src extends TargetActor {
    public void go(RP rp) throws Exception;
}
