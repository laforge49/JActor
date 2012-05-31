package org.agilewiki.jactor.lpc.exceptionsTest;

import org.agilewiki.jactor.Actor;
import org.agilewiki.jactor.RP;
import org.agilewiki.jactor.lpc.JLPCActor;
import org.agilewiki.jactor.lpc.Request;

public class Go2 extends Request<Object, GoReceiver> {
    public final static Go2 req = new Go2();

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
