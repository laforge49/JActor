package org.agilewiki.jactor.multithreadingTest;

import org.agilewiki.jactor.bind.JBRequest;
import org.agilewiki.jactor.components.JCActor;
import org.agilewiki.jactor.lpc.Request;

/**
 * Test code.
 */
public class PrintResponse<RESPONSE_TYPE> extends JBRequest<Object> {
    private Request<RESPONSE_TYPE, ?> request;
    private JCActor actor;

    public PrintResponse(Request<RESPONSE_TYPE, ?> request, JCActor actor) {
        this.request = request;
        this.actor = actor;
    }

    public Request<RESPONSE_TYPE, ?> getRequest() {
        return request;
    }

    public JCActor getActor() {
        return actor;
    }
}
