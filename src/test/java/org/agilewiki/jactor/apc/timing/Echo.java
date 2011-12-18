package org.agilewiki.jactor.apc.timing;

import org.agilewiki.jactor.apc.JAPCActor;
import org.agilewiki.jactor.apc.ResponseProcessor;
import org.agilewiki.jactor.concurrent.ThreadManager;

final public class Echo extends JAPCActor {

    public Echo(ThreadManager threadManager) {
        super(threadManager);
    }

    @Override
    protected void processRequest(Object data, ResponseProcessor responseProcessor)
            throws Exception {
        responseProcessor.process(null);
    }
}
