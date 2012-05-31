package org.agilewiki.jactor.counterTest;

import org.agilewiki.jactor.Actor;
import org.agilewiki.jactor.RP;
import org.agilewiki.jactor.lpc.JLPCActor;
import org.agilewiki.jactor.lpc.Request;

/**
 * Test code.
 */
final public class AddCount extends Request<Object, CounterActor> {
    public long number;

    @Override
    public void processRequest(JLPCActor targetActor, RP rp) throws Exception {
        CounterActor ca = (CounterActor) targetActor;
        ca.processRequest(this, rp);
    }

    @Override
    public boolean isTargetType(Actor targetActor) {
        return targetActor instanceof CounterActor;
    }
}
