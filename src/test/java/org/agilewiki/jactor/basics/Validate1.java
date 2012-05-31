package org.agilewiki.jactor.basics;

import org.agilewiki.jactor.Actor;
import org.agilewiki.jactor.RP;
import org.agilewiki.jactor.lpc.JLPCActor;
import org.agilewiki.jactor.lpc.Request;

/**
 * Test code.
 */
public class Validate1 extends Request<Boolean, Actor4> {
    public static final Validate1 req = new Validate1();

    @Override
    public void processRequest(JLPCActor targetActor, RP rp) throws Exception {
        Actor4 a = (Actor4) targetActor;
        a.processRequest(this, rp);
    }

    @Override
    public boolean isTargetType(Actor targetActor) {
        return targetActor instanceof Actor4;
    }
}
