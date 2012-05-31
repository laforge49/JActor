package org.agilewiki.jactor.lpc.exceptionsTest;

import org.agilewiki.jactor.RP;
import org.agilewiki.jactor.lpc.TargetActor;

public interface GoReceiver extends TargetActor {
    public void processRequest(Go1 request, RP rp)
            throws Exception;

    public void processRequest(Go2 request, RP rp)
            throws Exception;
}
