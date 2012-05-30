package org.agilewiki.jactor.lpc.timingTest;

import org.agilewiki.jactor.Actor;
import org.agilewiki.jactor.RP;
import org.agilewiki.jactor.lpc.JLPCActor;
import org.agilewiki.jactor.lpc.Request;

public class RealRequest extends Request<Object, RealRequestReceiver> {
    public final static RealRequest req = new RealRequest();

    @Override
    public boolean isTargetType(Actor targetActor) {
        return targetActor instanceof RealRequestReceiver;
    }

    @Override
    public void processRequest(JLPCActor targetActor, RP rp) throws Exception {
        RealRequestReceiver smDriver = (RealRequestReceiver) targetActor;
        smDriver.processRequest(this, rp);
    }
}
