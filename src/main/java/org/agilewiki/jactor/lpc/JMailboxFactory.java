package org.agilewiki.jactor.lpc;

import org.agilewiki.jactor.concurrent.JAThreadFactory;
import org.agilewiki.jactor.concurrent.JAThreadManager;
import org.agilewiki.jactor.concurrent.ThreadManager;

import java.util.concurrent.ThreadFactory;

/**
 * Implements MailboxFactory.
 */
public class JMailboxFactory extends JAThreadManager implements MailboxFactory {

    /**
     * Create a JMailboxFactory
     *
     * @param threadCount The number of concurrent to be used.
     * @return A new JMailboxFactory.
     */
    public static JMailboxFactory newMailboxFactory(int threadCount) {
        ThreadFactory threadFactory = new JAThreadFactory();
        return newMailboxFactory(threadCount, threadFactory);
    }

    /**
     * Create a JMailboxFactory
     *
     * @param threadCount   The number of concurrent to be used.
     * @param threadFactory Used to create the concurrent.
     * @return A new JMailboxFactory.
     */
    public static JMailboxFactory newMailboxFactory(int threadCount, ThreadFactory threadFactory) {
        JMailboxFactory mailboxFactory = new JMailboxFactory();
        mailboxFactory.start(threadCount, threadFactory);
        return mailboxFactory;
    }

    /**
     * Create a mailbox.
     *
     * @return A new mailbox.
     */
    @Override
    final public LPCMailbox createMailbox() {
        final JLPCMailbox mailbox = new JLPCMailbox(this);
        return mailbox;
    }

    /**
     * Create an asynchronous mailbox.
     *
     * @return A new asynchronous mailbox.
     */
    @Override
    final public LPCMailbox createAsyncMailbox() {
        final JLPCMailbox mailbox = new JLPCMailbox(this, true);
        return mailbox;
    }
}
