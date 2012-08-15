package org.agilewiki.jactor.multithreadingTest;

import org.agilewiki.jactor.lpc.JLPCActor;

/**
 * Test code.
 */
public class Greeter extends JLPCActor {
    public String hi() {
        return "Hello world!";
    }
}