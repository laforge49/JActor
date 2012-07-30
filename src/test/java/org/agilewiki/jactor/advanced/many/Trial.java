package org.agilewiki.jactor.advanced.many;

import org.agilewiki.jactor.Actor;
import org.agilewiki.jactor.RP;
import org.agilewiki.jactor.lpc.JLPCActor;
import org.agilewiki.jactor.lpc.Request;

public class Trial extends Request<Object, Driver> {
    public final static Trial req = new Trial();

    @Override
    public boolean isTargetType(Actor targetActor) {
        return targetActor instanceof Driver;
    }

    @Override
    public void processRequest(JLPCActor targetActor, RP rp) throws Exception {
        ((Driver) targetActor).trial(rp);
    }
}
