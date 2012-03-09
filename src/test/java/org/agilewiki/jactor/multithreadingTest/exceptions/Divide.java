package org.agilewiki.jactor.multithreadingTest.exceptions;

import org.agilewiki.jactor.Request;

/**
 * Test code.
 */
public class Divide extends Request<Integer> {
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
}
