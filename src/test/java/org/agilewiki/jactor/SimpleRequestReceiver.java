package org.agilewiki.jactor;

import org.agilewiki.jactor.lpc.TargetActor;

public interface SimpleRequestReceiver extends TargetActor {
    public void processRequest(SimpleRequest request, RP rp)
            throws Exception;
}
