package org.agilewiki.jactor.multithreadingTest;

import org.agilewiki.jactor.Actor;
import org.agilewiki.jactor.RP;
import org.agilewiki.jactor.lpc.JLPCActor;
import org.agilewiki.jactor.lpc.Request;

/**
 * Test code.
 */
public class PrintResponse<RESPONSE_TYPE> extends Request<Object, ResponsePrinter> {
    private Request<RESPONSE_TYPE, ?> request;
    private Actor actor;

    public PrintResponse(Request<RESPONSE_TYPE, ?> request, Actor actor) {
        this.request = request;
        this.actor = actor;
    }

    public Request<RESPONSE_TYPE, ?> getRequest() {
        return request;
    }

    public Actor getActor() {
        return actor;
    }

    @Override
    public boolean isTargetType(Actor targetActor) {
        return targetActor instanceof ResponsePrinter;
    }

    @Override
    public void processRequest(JLPCActor targetActor, RP rp) throws Exception {
        ((ResponsePrinter) targetActor).printResponse(request, actor, rp);
    }
}
