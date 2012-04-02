package org.agilewiki.jactor.lpc;

/**
 * Requests that can be called from outside an actor.
 */
abstract public class ExternallyCallableRequest<RESPONSE_TYPE, TARGET_TYPE extends TargetActor>
        extends ConstrainedRequest<RESPONSE_TYPE, TARGET_TYPE> {
}
