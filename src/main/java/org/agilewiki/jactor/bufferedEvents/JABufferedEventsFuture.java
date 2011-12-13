package org.agilewiki.jactor.bufferedEvents;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;

/**
 * Used mostly for testing, JABufferedEventsFuture is used to send
 * events to a BufferedEventsDestination, like JABufferedEventsActor, and then wait
 * for a return event.
 *
 * @param <E> The type of event.
 */
final public class JABufferedEventsFuture<E> implements BufferedEventsDestination<E> {
    private Semaphore done;
    private transient E result;

    /**
     * Send an event and then wait for the response, which is returned.
     *
     * @param destination Where the event is to be sent.
     * @param event       The event to be sent.
     */
    public E send(BufferedEventsDestination<E> destination, E event) {
        done = new Semaphore(0);
        ArrayList<E> bufferedEvents = new ArrayList<E>(1);
        bufferedEvents.add(event);
        destination.putBufferedEvents(bufferedEvents);
        try {
            done.acquire();
        } catch (InterruptedException e) {
        }
        done = null;
        return result;
    }

    /**
     * The putBufferedEvents method adds events to be processed.
     *
     * @param bufferedEvents The events to be processed.
     */
    @Override
    public void putBufferedEvents(ArrayList<E> bufferedEvents) {
        result = bufferedEvents.get(0);
        done.release();
    }
}
