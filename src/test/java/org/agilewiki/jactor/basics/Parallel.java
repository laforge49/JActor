package org.agilewiki.jactor.basics;

import org.agilewiki.jactor.Actor;
import org.agilewiki.jactor.RP;
import org.agilewiki.jactor.lpc.JLPCActor;
import org.agilewiki.jactor.lpc.Request;

/**
 * Test code.
 */
public class Parallel extends Request<Boolean, Actor5a> {
    public static final Parallel req = new Parallel();

    @Override
    public void processRequest(JLPCActor targetActor, RP rp) throws Exception {
        Actor5a a = (Actor5a) targetActor;
        a.processRequest(this, rp);
    }

    @Override
    public boolean isTargetType(Actor targetActor) {
        return targetActor instanceof Actor5a;
    }
}
