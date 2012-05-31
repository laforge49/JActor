package org.agilewiki.jactor.lpc.serverTest;

import org.agilewiki.jactor.Actor;
import org.agilewiki.jactor.RP;
import org.agilewiki.jactor.lpc.JLPCActor;
import org.agilewiki.jactor.lpc.Request;

/**
 * Test code.
 */
public class WorkRequest extends Request<Object, Worker> {
    public int id;

    @Override
    public void processRequest(JLPCActor targetActor, RP rp) throws Exception {
        Worker w = (Worker) targetActor;
        w.processRequest(this, rp);
    }

    @Override
    public boolean isTargetType(Actor targetActor) {
        return targetActor instanceof Worker;
    }
}
