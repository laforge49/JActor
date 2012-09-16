package org.agilewiki.jactor.lpc;

import org.agilewiki.jactor.Mailbox;
import org.agilewiki.jactor.MailboxFactory;
import org.agilewiki.jactor.apc.JAMessage;
import org.agilewiki.jactor.apc.JAPCMailbox;
import org.agilewiki.jactor.bufferedEvents.BufferedEventsQueue;

/**
 * Implements Mailbox.
 */
final public class JLPCMailbox extends JAPCMailbox implements Mailbox {

    /**
     * Used to create mailboxes.
     */
    private MailboxFactory mailboxFactory;

    /**
     * Create a JLPCMailbox.
     * Use this constructor when providing an implementation of BufferedEventsQueue
     * other than JABufferedEventsQueue.
     *
     * @param eventQueue     The lower-level mailbox which transports messages as 1-way events.
     * @param mailboxFactory Provides a thread for processing dispatched events.
     */
    public JLPCMailbox(final BufferedEventsQueue<JAMessage> eventQueue,
                       final MailboxFactory mailboxFactory) {
        super(eventQueue, mailboxFactory);
        this.mailboxFactory = mailboxFactory;
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
        super(mailboxFactory.getThreadManager(), async, mailboxFactory);
        this.mailboxFactory = mailboxFactory;
    }

    /**
     * Create a JLPCMailbox.
     *
     * @param mailboxFactory Provides a thread for processing dispatched events.
     */
    public JLPCMailbox(MailboxFactory mailboxFactory) {
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
}
