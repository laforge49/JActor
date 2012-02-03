package org.agilewiki.jactor.bind;

/**
 * A request that can be passed to an actor for processing
 * via the Internals.call or Actor.acceptCall methods,
 * but only until a non-initialization request is received.
 */
public class InitializationRequest extends ConstrainedRequest {}
