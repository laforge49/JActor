package org.agilewiki.jactor.lpc;

import org.agilewiki.jactor.apc.APCMailbox;
import org.agilewiki.jactor.apc.JAPCMailbox;
import org.agilewiki.jactor.apc.JAPCMessage;
import org.agilewiki.jactor.apc.JAPCRequest;
import org.agilewiki.jactor.bufferedEvents.BufferedEventsDestination;
import org.agilewiki.jactor.bufferedEvents.BufferedEventsQueue;
import org.agilewiki.jactor.concurrent.ThreadManager;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Implements LPCMailbox.
 */
final public class JLPCMailbox extends JAPCMailbox implements LPCMailbox {

    /**
     * Tracks which mailbox has control. If an exchange can gain control
     * over another exchange, it can send requests to it synchronously.
     */
    private final AtomicReference<LPCMailbox> atomicControl = new AtomicReference<LPCMailbox>();

    /**
     * Set to true when all requests are to be processed asynchronously.
     */
    private boolean async;

    /**
     * Create a JLPCMailbox.
     * Use this constructor when providing an implementation of BufferedEventsQueue
     * other than JABufferedEventsQueue.
     *
     * @param eventQueue The lower-level mailbox which transports messages as 1-way events.
     * @param async      Set to true when all requests are to be processed asynchronously.
     */
    public JLPCMailbox(BufferedEventsQueue<JAPCMessage> eventQueue, boolean async) {
        super(eventQueue);
        this.async = async;
    }

    /**
     * Create a JLPCMailbox.
     *
     * @param threadManager Provides a thread for processing dispatched events.
     * @param async         Set to true when all requests are to be processed asynchronously.
     */
    public JLPCMailbox(ThreadManager threadManager, boolean async) {
        super(threadManager);
        this.async = async;
    }

    /**
     * Create a JLPCMailbox.
     *
     * @param threadManager Provides a thread for processing dispatched events.
     */
    public JLPCMailbox(ThreadManager threadManager) {
        this(threadManager, false);
    }

    /**
     * Returns the controlling mailbox, or null.
     */
    public final LPCMailbox controllingMailbox = atomicControl.get();
}
