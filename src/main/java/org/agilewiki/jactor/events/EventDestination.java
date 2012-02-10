package org.agilewiki.jactor.events;

/**
 * An EventDestination receives an event from objects operating
 * on a different thread.
 *
 * @param <E> The type of event.
 */
public interface EventDestination<E> {
    /**
     * The putEvent method adds an events to the queue of events to be processed.
     *
     * @param event The events to be processed.
     */
    public void putEvent(E event);
}
