package org.agilewiki.jactor.lpc.timingTest;

import org.agilewiki.jactor.Actor;
import org.agilewiki.jactor.RP;
import org.agilewiki.jactor.lpc.JLPCActor;
import org.agilewiki.jactor.lpc.Request;

/**
 * A wrapper of a request to be sent to multiple actors.
 */
public class Run1Parallel extends Request<Object, JAParallel> {
    /**
     * The wrapped request;
     */
    private Request request;

    /**
     * Returns the wrapped request.
     *
     * @return The wrapped request.
     */
    public Request getRequest() {
        return request;
    }

    /**
     * Create the request.
     *
     * @param request the request to be run in parallel.
     */
    public Run1Parallel(Request request) {
        this.request = request;
    }

    @Override
    public boolean isTargetType(Actor targetActor) {
        return targetActor instanceof JAParallel;
    }

    @Override
    public void processRequest(JLPCActor targetActor, RP rp) throws Exception {
        ((JAParallel) targetActor).run1Parallel(request, rp);
    }
}
