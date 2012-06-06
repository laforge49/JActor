package org.agilewiki.jactor.multithreadingTest;

import org.agilewiki.jactor.RP;
import org.agilewiki.jactor.lpc.JLPCActor;

/**
 * Test code.
 */
public class Greeter extends JLPCActor {
    public String hi() {
        return "Hello world!";
    }

    @Override
    protected void processRequest(Object request, RP rp)
            throws Exception {
        Class reqcls = request.getClass();

        if (reqcls == Hi.class) {
            rp.processResponse(hi());
            return;
        }

        throw new UnsupportedOperationException(reqcls.getName());
    }
}