package org.agilewiki.jactor.synchronousProgrammingTest;

import org.agilewiki.jactor.Actor;
import org.agilewiki.jactor.lpc.SynchronousRequest;

/**
 * Test code.
 */
public class PrintResponse extends SynchronousRequest<Object, ResponsePrinter> {
    private SynchronousRequest request;
    private Actor actor;

    public PrintResponse(SynchronousRequest request, Actor actor) {
        this.request = request;
        this.actor = actor;
    }

    public SynchronousRequest getRequest() {
        return request;
    }

    public Actor getActor() {
        return actor;
    }

    @Override
    protected Object _call(ResponsePrinter targetActor)
            throws Exception {
        targetActor.printResponse(request, actor);
        return null;
    }

    @Override
    public boolean isTargetType(Actor targetActor) {
        return targetActor instanceof ResponsePrinter;
    }
}
