package org.agilewiki.jactor.multithreadingTest.exceptions;

import org.agilewiki.jactor.Actor;
import org.agilewiki.jactor.lpc.Request;

/**
 * Test code.
 */
public class Divide extends Request<Integer, Divider> {
    private int n;
    private int d;

    public int getN() {
        return n;
    }

    public int getD() {
        return d;
    }

    public Divide(int n, int d) {
        this.n = n;
        this.d = d;
    }

    @Override
    public boolean isTargetType(Actor targetActor) {
        return targetActor instanceof Divider;
    }
}
