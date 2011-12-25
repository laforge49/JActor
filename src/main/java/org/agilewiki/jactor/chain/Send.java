package org.agilewiki.jactor.chain;

import org.agilewiki.jactor.Actor;

/**
 * Adds a call to send to a Chain.
 */
public interface Send {
    public Actor getTargetActor();

    public Object getRequest();

    public String getResultName();
}
