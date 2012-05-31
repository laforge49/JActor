package org.agilewiki.jactor.basics;

import org.agilewiki.jactor.RP;
import org.agilewiki.jactor.lpc.TargetActor;

/**
 * Test code.
 */
public interface Greeter extends TargetActor {
    void processRequest(Greet1 request, final RP rp) throws Exception;
}
