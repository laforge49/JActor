package org.agilewiki.jactor.lpc.syncTiming;

import org.agilewiki.jactor.Actor;
import org.agilewiki.jactor.RP;
import org.agilewiki.jactor.lpc.JLPCActor;
import org.agilewiki.jactor.lpc.Request;

public class DoSender extends Request<Object, Sender> {
    public final static DoSender req = new DoSender();

    @Override
    public boolean isTargetType(Actor targetActor) {
        return targetActor instanceof Sender;
    }

    @Override
    public void processRequest(JLPCActor targetActor, RP rp) throws Exception {
        ((Sender) targetActor).sender(rp);
    }
}
