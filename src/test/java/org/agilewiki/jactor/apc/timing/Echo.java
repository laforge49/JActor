package org.agilewiki.jactor.apc.timing;

import org.agilewiki.jactor.apc.JAPCActor;
import org.agilewiki.jactor.apc.ResponseDestination;
import org.agilewiki.jactor.concurrent.ThreadManager;

final public class Echo extends JAPCActor {

    public Echo(ThreadManager threadManager) {
        super(threadManager);
    }

    @Override
    protected void processRequest(Object data, ResponseDestination responseDestination)
            throws Exception {
        responseDestination.process(null);
    }
}
