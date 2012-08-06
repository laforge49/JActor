package org.agilewiki.jactor.lpc.syncTiming;

import org.agilewiki.jactor.Actor;
import org.agilewiki.jactor.RP;
import org.agilewiki.jactor.lpc.JLPCActor;
import org.agilewiki.jactor.lpc.Request;

public class DoEcho extends Request<Object, Echo> {
    public final static DoEcho req = new DoEcho();

    @Override
    public boolean isTargetType(Actor targetActor) {
        return targetActor instanceof Echo;
    }

    @Override
    public void processRequest(JLPCActor targetActor, RP rp) throws Exception {
        ((Echo) targetActor).echo();
        rp.processResponse(null);
    }
}
