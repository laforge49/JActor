package org.agilewiki.jactor.basics;

import org.agilewiki.jactor.Actor;
import org.agilewiki.jactor.RP;
import org.agilewiki.jactor.lpc.JLPCActor;
import org.agilewiki.jactor.lpc.Request;

/**
 * Test code.
 */
public class Delay extends Request<Object, Actor5> {
    public static final Delay req = new Delay();

    @Override
    public void processRequest(JLPCActor targetActor, RP rp) throws Exception {
        Actor5 a = (Actor5) targetActor;
        a.processRequest(this, rp);
    }

    @Override
    public boolean isTargetType(Actor targetActor) {
        return targetActor instanceof Actor5;
    }
}
