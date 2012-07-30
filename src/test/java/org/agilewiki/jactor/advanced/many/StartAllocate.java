package org.agilewiki.jactor.advanced.many;

import org.agilewiki.jactor.Actor;
import org.agilewiki.jactor.RP;
import org.agilewiki.jactor.lpc.JLPCActor;
import org.agilewiki.jactor.lpc.Request;

public class StartAllocate extends Request<Object, AllocateDriver> {
    public final static StartAllocate req = new StartAllocate();

    @Override
    public boolean isTargetType(Actor targetActor) {
        return targetActor instanceof AllocateDriver;
    }

    @Override
    public void processRequest(JLPCActor targetActor, RP rp) throws Exception {
        ((AllocateDriver) targetActor).startAllocate(rp);
    }
}
