package org.agilewiki.jactor.lpc.calculatorTest;

import org.agilewiki.jactor.Actor;
import org.agilewiki.jactor.RP;
import org.agilewiki.jactor.lpc.JLPCActor;
import org.agilewiki.jactor.lpc.Request;

/**
 * Test code.
 */
public class Clear extends Request<Integer, _Calculator> {

    @Override
    public void processRequest(JLPCActor targetActor, RP rp) throws Exception {
        _Calculator c = (_Calculator) targetActor;
        c.clear(this, rp);
    }

    @Override
    public boolean isTargetType(Actor targetActor) {
        return targetActor instanceof _Calculator;
    }

}
