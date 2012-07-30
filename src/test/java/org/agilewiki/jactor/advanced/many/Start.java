package org.agilewiki.jactor.advanced.many;

import org.agilewiki.jactor.Actor;
import org.agilewiki.jactor.RP;
import org.agilewiki.jactor.lpc.JLPCActor;
import org.agilewiki.jactor.lpc.Request;

public class Start extends Request<Object, Driver> {
    public final static Start req = new Start();

    @Override
    public boolean isTargetType(Actor targetActor) {
        return targetActor instanceof Driver;
    }

    @Override
    public void processRequest(JLPCActor targetActor, RP rp) throws Exception {
        ((Driver) targetActor).start(rp);
    }
}
