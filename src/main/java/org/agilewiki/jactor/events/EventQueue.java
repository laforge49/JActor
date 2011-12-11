package org.agilewiki.jactor.events;

/**
 * An EventQueue receives events, queues them,
 * and then processes them on another thread.
 *
 * @param <E>
 */
public interface EventQueue<E> extends EventDispatcher<E>, EventDestination<E> {}
