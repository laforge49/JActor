package org.agilewiki.jactor.components.pubsub.timing;

import org.agilewiki.jactor.bind.Request;

final public class Timing extends Request {
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
