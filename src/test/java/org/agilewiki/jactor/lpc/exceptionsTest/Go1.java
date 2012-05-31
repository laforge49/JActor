package org.agilewiki.jactor.lpc.exceptionsTest;

import org.agilewiki.jactor.Actor;
import org.agilewiki.jactor.RP;
import org.agilewiki.jactor.lpc.JLPCActor;
import org.agilewiki.jactor.lpc.Request;

public class Go1 extends Request<Object, GoReceiver> {
    public final static Go1 req = new Go1();

    @Override
    public boolean isTargetType(Actor targetActor) {
        return targetActor instanceof GoReceiver;
    }

    @Override
    public void processRequest(JLPCActor targetActor, RP rp) throws Exception {
        GoReceiver smDriver = (GoReceiver) targetActor;
        smDriver.processRequest(this, rp);
    }
}
