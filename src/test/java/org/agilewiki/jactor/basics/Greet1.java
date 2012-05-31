package org.agilewiki.jactor.basics;

import org.agilewiki.jactor.Actor;
import org.agilewiki.jactor.RP;
import org.agilewiki.jactor.lpc.JLPCActor;
import org.agilewiki.jactor.lpc.Request;

/**
 * Test code.
 */
public class Greet1 extends Request<Object, Greeter> {
    public static final Greet1 req = new Greet1();

    @Override
    public void processRequest(JLPCActor targetActor, RP rp) throws Exception {
        Greeter a = (Greeter) targetActor;
        a.processRequest(this, rp);
    }

    @Override
    public boolean isTargetType(Actor targetActor) {
        return targetActor instanceof Greeter;
    }
}
