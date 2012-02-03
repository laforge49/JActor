package org.agilewiki.jactor.bind;

/**
 * A request that can be passed to an actor for processing
 * via the Internals.call or Actor.acceptCall methods,
 * but only when sender and receiver use the same mailbox.
 */
public class SynchronousRequest extends ConstrainedRequest {
}
