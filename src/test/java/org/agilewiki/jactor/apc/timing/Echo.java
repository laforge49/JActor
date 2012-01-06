package org.agilewiki.jactor.apc.timing;

import org.agilewiki.jactor.ResponseProcessor;
import org.agilewiki.jactor.apc.JAPCActor;
import org.agilewiki.jactor.concurrent.ThreadManager;

final public class Echo extends JAPCActor {

    public Echo(ThreadManager threadManager) {
        super(threadManager);
    }

    @Override
    public void processRequest(Object unwrappedRequest, ResponseProcessor responseProcessor)
            throws Exception {
        responseProcessor.process(null);
    }
}
