/**
 * <p>
 * Request message class names are used as the keys in a table of Binding objects,
 * giving the application developer control over what happens when an actor receives a message.
 * </p>
 * <p>
 * Delegation of requests is also supported.
 * If a JBActor actor receives a request of a type that it does not recognize
 * and that actor has been assigned a parent actor,
 * then the request is immediately forwarded to the parent actor.
 * </p>
 */
package org.agilewiki.jactor.bind;