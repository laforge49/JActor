package org.agilewiki.jactor.advanced.many;

import org.agilewiki.jactor.Actor;
import org.agilewiki.jactor.RP;
import org.agilewiki.jactor.lpc.JLPCActor;
import org.agilewiki.jactor.lpc.Request;

public class StartRelease extends Request<Object, ReleaseDriver> {
    public final static StartRelease req = new StartRelease();

    @Override
    public boolean isTargetType(Actor targetActor) {
        return targetActor instanceof ReleaseDriver;
    }

    @Override
    public void processRequest(JLPCActor targetActor, RP rp) throws Exception {
        ((ReleaseDriver) targetActor).startRelease(rp);
    }
}
