package org.agilewiki.jactor.lpc.calculatorTest;

import org.agilewiki.jactor.Actor;
import org.agilewiki.jactor.RP;
import org.agilewiki.jactor.lpc.JLPCActor;
import org.agilewiki.jactor.lpc.Request;

/**
 * Test code.
 */
final public class Factorial extends Request<Integer, FactorialCalculation> {
    public Factorial(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    private int value;

    @Override
    public void processRequest(JLPCActor targetActor, RP rp) throws Exception {
        FactorialCalculation fc = (FactorialCalculation) targetActor;
        fc.processRequest(this, rp);
    }

    @Override
    public boolean isTargetType(Actor targetActor) {
        return targetActor instanceof FactorialCalculation;
    }
}
