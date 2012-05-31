package org.agilewiki.jactor.iteratorTest.factorialTest;

import org.agilewiki.jactor.Actor;
import org.agilewiki.jactor.RP;
import org.agilewiki.jactor.lpc.JLPCActor;
import org.agilewiki.jactor.lpc.Request;

/**
 * Test code.
 */
public class Multiply extends Request<Integer, Multiplier> {
    public int a;
    public int b;

    @Override
    public void processRequest(JLPCActor targetActor, RP rp) throws Exception {
        Multiplier m = (Multiplier) targetActor;
        m.processRequest(this, rp);
    }

    @Override
    public boolean isTargetType(Actor targetActor) {
        return targetActor instanceof Multiplier;
    }
}
