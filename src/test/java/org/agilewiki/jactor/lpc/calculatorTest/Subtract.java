package org.agilewiki.jactor.lpc.calculatorTest;

import org.agilewiki.jactor.Actor;
import org.agilewiki.jactor.RP;
import org.agilewiki.jactor.lpc.JLPCActor;
import org.agilewiki.jactor.lpc.Request;

/**
 * Test code.
 */
public class Subtract extends Request<Integer, _Calculator> {

    @Override
    public void processRequest(JLPCActor targetActor, RP rp) throws Exception {
        _Calculator c = (_Calculator) targetActor;
        c.subtract(this, rp);
    }

    @Override
    public boolean isTargetType(Actor targetActor) {
        return targetActor instanceof _Calculator;
    }

    public Subtract(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    private int value;
}
