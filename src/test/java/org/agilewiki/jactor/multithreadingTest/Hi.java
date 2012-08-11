package org.agilewiki.jactor.multithreadingTest;

import org.agilewiki.jactor.Actor;
import org.agilewiki.jactor.RP;
import org.agilewiki.jactor.lpc.JLPCActor;
import org.agilewiki.jactor.lpc.Request;

/**
 * Test code.
 */
public class Hi extends Request<String, Greeter> {
    @Override
    public boolean isTargetType(Actor targetActor) {
        return targetActor instanceof Greeter;
    }

    @Override
    public void processRequest(JLPCActor targetActor, RP rp) throws Exception {
        rp.processResponse(((Greeter) targetActor).hi());
    }
}