package org.agilewiki.jactor.advanced.many;

import org.agilewiki.jactor.Actor;
import org.agilewiki.jactor.RP;
import org.agilewiki.jactor.lpc.JLPCActor;
import org.agilewiki.jactor.lpc.Request;

public class Allocate extends Request<Object, Doer> {
    public final static Allocate req = new Allocate();

    @Override
    public boolean isTargetType(Actor targetActor) {
        return targetActor instanceof AllocateDriver;
    }

    @Override
    public void processRequest(JLPCActor targetActor, RP rp) throws Exception {
        ((Doer) targetActor).allocate();
        rp.processResponse(null);
    }
}
