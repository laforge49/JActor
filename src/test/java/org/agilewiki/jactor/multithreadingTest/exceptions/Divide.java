package org.agilewiki.jactor.multithreadingTest.exceptions;

import org.agilewiki.jactor.bind.AsyncRequest;

/**
 * Test code.
 */
public class Divide extends AsyncRequest<Integer> {
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
