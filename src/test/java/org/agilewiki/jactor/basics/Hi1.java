package org.agilewiki.jactor.basics;

import org.agilewiki.jactor.Actor;
import org.agilewiki.jactor.RP;
import org.agilewiki.jactor.lpc.JLPCActor;
import org.agilewiki.jactor.lpc.Request;

/**
 * Test code.
 */
public class Hi1 extends Request<String, Actor1> {
    public static final Hi1 req = new Hi1();

    @Override
    public void processRequest(JLPCActor targetActor, RP rp) throws Exception {
        Actor1 a = (Actor1) targetActor;
        a.processRequest(this, rp);
    }

    @Override
    public boolean isTargetType(Actor targetActor) {
        return targetActor instanceof Actor1;
    }
}
