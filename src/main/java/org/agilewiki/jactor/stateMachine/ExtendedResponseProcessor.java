package org.agilewiki.jactor.stateMachine;

import org.agilewiki.jactor.ResponseProcessor;

/**
 * Supports processing sensitive to how a response is returned.
 */
abstract public class ExtendedResponseProcessor implements ResponseProcessor {
    /**
     * Set true when a response is received synchronously.
     */
    public boolean sync;

    /**
     * Set true when a response is received asynchronously.
     */
    public boolean async;
}
