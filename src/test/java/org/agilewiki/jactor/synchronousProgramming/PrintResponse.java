package org.agilewiki.jactor.synchronousProgramming;

import org.agilewiki.jactor.bind.SynchronousRequest;
import org.agilewiki.jactor.components.JCActor;

public class PrintResponse<RESPONSE_TYPE> extends SynchronousRequest {
    private SynchronousRequest<RESPONSE_TYPE> request;
    private JCActor actor;

    public PrintResponse(SynchronousRequest<RESPONSE_TYPE> request, JCActor actor) {
        this.request = request;
        this.actor = actor;
    }

    public SynchronousRequest<RESPONSE_TYPE> getRequest() {
        return request;
    }

    public JCActor getActor() {
        return actor;
    }
}
