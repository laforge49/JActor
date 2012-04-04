package org.agilewiki.jactor.multithreadingTest.exceptions;

import org.agilewiki.jactor.Actor;
import org.agilewiki.jactor.lpc.SynchronousRequest;

/**
 * Test code.
 */
public class SyncDivide extends SynchronousRequest<Integer, Divider> {
    protected int n;
    protected int d;

    public int getN() {
        return n;
    }

    public int getD() {
        return d;
    }

    public SyncDivide(int n, int d) {
        this.n = n;
        this.d = d;
    }

    @Override
    protected Integer _call(Divider targetActor) throws Exception {
        return targetActor.syncDivide(n, d);
    }

    @Override
    public boolean isTargetType(Actor targetActor) {
        return targetActor instanceof Divider;
    }
}
