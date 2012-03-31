package org.agilewiki.jactor.components.pubsub.timingTest;

import org.agilewiki.jactor.Actor;
import org.agilewiki.jactor.bind.AsyncRequest;
import org.agilewiki.jactor.parallel.JAParallel;

/**
 * Test code.
 */
final public class Timing extends AsyncRequest<Object> {
    private int count;
    private int burst;

    public Timing(int count, int burst) {
        this.count = count;
        this.burst = burst;
    }

    public int getCount() {
        return count;
    }

    public int getBurst() {
        return burst;
    }

    @Override
    protected boolean isTargetType(Actor targetActor) {
        return targetActor instanceof JAParallel;
    }
}
