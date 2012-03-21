package org.agilewiki.jactor.synchronousProgrammingTest;

import org.agilewiki.jactor.bind.JBSynchronousRequest;
import org.agilewiki.jactor.components.JCActor;

/**
 * Test code.
 */
public class PrintResponse<RESPONSE> extends JBSynchronousRequest<Object> {
    private JBSynchronousRequest<RESPONSE> request;
    private JCActor actor;

    public PrintResponse(JBSynchronousRequest<RESPONSE> request, JCActor actor) {
        this.request = request;
        this.actor = actor;
    }

    public JBSynchronousRequest<RESPONSE> getRequest() {
        return request;
    }

    public JCActor getActor() {
        return actor;
    }
}
