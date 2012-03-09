package org.agilewiki.jactor.components.pubsub.timingTest;

import org.agilewiki.jactor.Request;

/**
 * Test code.
 */
final public class Timing extends Request<Object> {
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
}
