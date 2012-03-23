package org.agilewiki.jactor.parallel;

import org.agilewiki.jactor.Actor;
import org.agilewiki.jactor.lpc.Request;

/**
 * A wrapper of an array of requests to be sent to multiple actors.
 */
public class RunParallel extends Request<Object, JAParallel> {
    /**
     * The wrapped requests;
     */
    private Request[] requests;

    /**
     * Returns the wrapped requests.
     *
     * @return The wrapped requests.
     */
    public Request[] getRequests() {
        return requests;
    }

    /**
     * Create the request.
     *
     * @param requests the requests to be run in parallel.
     */
    public RunParallel(Request[] requests) {
        this.requests = requests;
    }

    @Override
    protected boolean isTargetType(Actor targetActor) {
        return targetActor instanceof JAParallel;
    }
}
