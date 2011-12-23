package org.agilewiki.jactor.lpc;

import org.agilewiki.jactor.apc.JAPCMailbox;
import org.agilewiki.jactor.apc.JAPCMessage;
import org.agilewiki.jactor.bufferedEvents.BufferedEventsDestination;
import org.agilewiki.jactor.bufferedEvents.BufferedEventsQueue;
import org.agilewiki.jactor.concurrent.ThreadManager;

import java.util.concurrent.atomic.AtomicReference;

/**
 * Implements LPCMailbox.
 */
public class JLPCMailbox extends JAPCMailbox implements LPCMailbox {

    /**
     * Tracks which mailbox has control. If an exchange can gain control
     * over another exchange, it can send requests to it synchronously.
     */
    final private AtomicReference<LPCMailbox> atomicControl = new AtomicReference<LPCMailbox>();

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
    public JLPCMailbox(final BufferedEventsQueue<JAPCMessage> eventQueue,
                       final boolean async) {
        super(eventQueue);
        this.async = async;
    }

    /**
     * Create a JLPCMailbox.
     *
     * @param threadManager Provides a thread for processing dispatched events.
     * @param async         Set to true when all requests are to be processed asynchronously.
     */
    public JLPCMailbox(final ThreadManager threadManager,
                       final boolean async) {
        super(threadManager);
        this.async = async;
    }

    /**
     * Create a JLPCMailbox.
     *
     * @param threadManager Provides a thread for processing dispatched events.
     */
    public JLPCMailbox(final ThreadManager threadManager) {
        this(threadManager, false);
    }

    /**
     * Returns true when all requests are to be processed asynchronously.
     *
     * @return True when all requests are to be processed asynchronously.
     */
    final public boolean isAsync() {
        return async;
    }

    /**
     * Returns the controlling mailbox, or null.
     */
    @Override
    final public LPCMailbox getControllingMailbox() {
        return atomicControl.get();
    }

    /**
     * Gains control over the mailbox.
     *
     * @param srcControllingMailbox The mailbox gaining control.
     * @return True when control was acquired.
     */
    @Override
    final public boolean acquireControl(final LPCMailbox srcControllingMailbox) {
        return atomicControl.compareAndSet(null, srcControllingMailbox);
    }

    /**
     * Relinquish control over the mailbox.
     */
    @Override
    final public void relinquishControl() {
        atomicControl.set(null);
    }

    /**
     * The dispatchMessages method processes any messages in the queue.
     * True is returned if any messages were actually processed.
     */
    @Override
    final public boolean dispatchEvents() {
        if (async) return super.dispatchEvents();
        boolean dispatched = false;
        if (atomicControl.compareAndSet(null, this)) {
            try {
                dispatched = super.dispatchEvents();
            } finally {
                atomicControl.set(null);
            }
        }
        return dispatched;
    }

    /**
     * Dispatch any enqueued requests, if possible.
     *
     * @param controllingMailbox The mailbox that was just in control.
     */
    final public void dispatchRemaining(final LPCMailbox controllingMailbox) {
        while (!isEmpty()) {
            if (getControllingMailbox() == controllingMailbox) {
                super.dispatchEvents();
            } else if (acquireControl(controllingMailbox)) {
                try {
                    super.dispatchEvents();
                } finally {
                    relinquishControl();
                }
            } else return;
        }
    }
}
