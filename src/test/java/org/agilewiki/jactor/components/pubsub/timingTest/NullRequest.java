package org.agilewiki.jactor.components.pubsub.timingTest;

import org.agilewiki.jactor.Actor;
import org.agilewiki.jactor.lpc.Request;
import org.agilewiki.jactor.parallel.JAParallel;

/**
 * Test code.
 */
final public class NullRequest extends Request<Object, JAParallel> {
    @Override
    protected boolean isTargetType(Actor targetActor) {
        return targetActor instanceof JAParallel;
    }
}
