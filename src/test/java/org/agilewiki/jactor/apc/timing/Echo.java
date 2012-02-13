package org.agilewiki.jactor.apc.timing;

import org.agilewiki.jactor.RP;
import org.agilewiki.jactor.apc.JAPCActor;
import org.agilewiki.jactor.concurrent.ThreadManager;

final public class Echo extends JAPCActor {

    public Echo(ThreadManager threadManager) {
        super(threadManager);
    }

    @Override
    public void processRequest(Object unwrappedRequest, RP responseProcessor)
            throws Exception {
        responseProcessor.process(null);
    }

    /**
     * Returns true when the concurrent data of the actor, or its parent, contains the named data item.
     *
     * @param name The key for the data item.
     * @return True when the concurrent data of the actor, or its parent, contains the named data item.
     */
    @Override
    public boolean hasDataItem(String name) {
        return false;
    }
}
