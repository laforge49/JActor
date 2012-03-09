package org.agilewiki.jactor.multithreadingTest;

import org.agilewiki.jactor.Request;
import org.agilewiki.jactor.components.JCActor;

public class PrintResponse<RESPONSE_TYPE> extends Request<Object> {
    private Request<RESPONSE_TYPE> request;
    private JCActor actor;

    public PrintResponse(Request<RESPONSE_TYPE> request, JCActor actor) {
        this.request = request;
        this.actor = actor;
    }

    public Request<RESPONSE_TYPE> getRequest() {
        return request;
    }

    public JCActor getActor() {
        return actor;
    }
}
