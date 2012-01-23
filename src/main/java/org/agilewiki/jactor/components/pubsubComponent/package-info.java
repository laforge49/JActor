/**
 * <p>
 *     One possible implementat of publish/subscribe.
 * </p>
 * <p>
 *     This package uses 2-way messaging, with the advantage that a publish
 *     operation returns no result until the message has been processed by
 *     all subscribers.
 * </p>
 * <p>
 *     There is no defined order for publishing requests to subscribers.
 *     And like JAParallel, requests are published in parallel.
 * </p>
 */
package org.agilewiki.jactor.components.pubsubComponent;
