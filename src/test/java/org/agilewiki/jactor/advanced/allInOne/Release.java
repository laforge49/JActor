package org.agilewiki.jactor.advanced.allInOne;

import org.agilewiki.jactor.RP;
import org.agilewiki.jactor.lpc.JLPCActor;

public class Release extends AllInOneReq {
    public final static Release req = new Release();

    @Override
    public void processRequest(JLPCActor targetActor, RP rp) throws Exception {
        ((AllInOne) targetActor).release();
    }
}
