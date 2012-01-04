package org.agilewiki.jactor.lpc;

import org.agilewiki.jactor.Mailbox;
import org.agilewiki.jactor.MailboxFactory;
import org.agilewiki.jactor.apc.JAMessage;
import org.agilewiki.jactor.apc.JAPCMailbox;
import org.agilewiki.jactor.bufferedEvents.BufferedEventsQueue;

import java.util.concurrent.atomic.AtomicReference;

/**
 * Implements Mailbox.
 */
final public class JLPCMailbox extends JAPCMailbox implements Mailbox {

    /**
     * Used to create mailboxes.
     */
    private MailboxFactory mailboxFactory;

    /**
     * Tracks which mailbox has control. If an exchange can gain control
     * over another exchange, it can send requests to it synchronously.
     */
    final private AtomicReference<Mailbox> atomicControl = new AtomicReference<Mailbox>();

    /**
     * Set to true when all requests are to be processed asynchronously.
     */
    private boolean async;

    /**
     * Create a JLPCMailbox.
     * Use this constructor when providing an implementation of BufferedEventsQueue
     * other than JABufferedEventsQueue.
     *
     * @param eventQueue     The lower-level mailbox which transports messages as 1-way events.
     * @param mailboxFactory Provides a thread for processing dispatched events.
     * @param async          Set to true when all requests are to be processed asynchronously.
     */
    public JLPCMailbox(final BufferedEventsQueue<JAMessage> eventQueue,
                       final MailboxFactory mailboxFactory,
                       final boolean async) {
        super(eventQueue);
        this.mailboxFactory = mailboxFactory;
        this.async = async;
    }

    /**
     * Create a JLPCMailbox.
     *
     * @param mailboxFactory Provides a thread for processing dispatched events.
     * @param async          Set to true when requests from other mailboxes
     *                       are to be processed asynchronously.
     */
    public JLPCMailbox(final MailboxFactory mailboxFactory,
                       final boolean async) {
        super(mailboxFactory.getThreadManager());
        this.mailboxFactory = mailboxFactory;
        this.async = async;
    }

    /**
     * Create a JLPCMailbox.
     *
     * @param mailboxFactory Provides a thread for processing dispatched events.
     */
    public JLPCMailbox(final MailboxFactory mailboxFactory) {
        this(mailboxFactory, false);
    }

    /**
     * Returns the mailbox factory.
     *
     * @return The mailbox factory.
     */
    @Override
    public MailboxFactory getMailboxFactory() {
        return mailboxFactory;
    }

    /**
     * Returns true when all requests are to be processed asynchronously.
     *
     * @return True when all requests are to be processed asynchronously.
     */
    public boolean isAsync() {
        return async;
    }

    /**
     * Returns the controlling mailbox, or null.
     */
    @Override
    public Mailbox getControllingMailbox() {
        Mailbox c = atomicControl.get();
        if (c == null) return this;
        return c;
    }

    /**
     * Gains control over the mailbox.
     *
     * @param srcControllingMailbox The mailbox gaining control.
     * @return True when control was acquired.
     */
    @Override
    public boolean acquireControl(final Mailbox srcControllingMailbox) {
        return atomicControl.compareAndSet(null, srcControllingMailbox);
    }

    /**
     * Relinquish control over the mailbox.
     */
    @Override
    public void relinquishControl() {
        atomicControl.set(null);
    }

    /**
     * The dispatchMessages method processes any messages in the queue.
     * True is returned if any messages were actually processed.
     */
    @Override
    public boolean dispatchEvents() {
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
    public void dispatchRemaining(final Mailbox controllingMailbox) {
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
