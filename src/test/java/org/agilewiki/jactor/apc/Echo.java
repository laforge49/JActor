package org.agilewiki.jactor.apc;

import org.agilewiki.jactor.concurrent.ThreadManager;

public class Echo extends JAPCActor {

    public Echo(ThreadManager threadManager) {
        super(threadManager);
    }

    @Override
    protected void processRequest(Object data, ResponseDestination responseDestination) {
        responseDestination.process(null);
    }
}
