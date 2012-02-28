package org.agilewiki.jactor.synchronousProgramming;

import org.agilewiki.jactor.bind.SynchronousRequest;
import org.agilewiki.jactor.components.JCActor;

public class PrintResponse<RESPONSE> extends SynchronousRequest<Object> {
    private SynchronousRequest<RESPONSE> request;
    private JCActor actor;

    public PrintResponse(SynchronousRequest<RESPONSE> request, JCActor actor) {
        this.request = request;
        this.actor = actor;
    }

    public SynchronousRequest<RESPONSE> getRequest() {
        return request;
    }

    public JCActor getActor() {
        return actor;
    }
}
