package org.agilewiki.jactor;

import org.agilewiki.jactor.concurrent.JAThreadManager;
import org.agilewiki.jactor.concurrent.ThreadManager;
import org.agilewiki.jactor.lpc.JLPCMailbox;

/**
 * Implements MailboxFactory.
 */
final public class JAMailboxFactory implements MailboxFactory {

    /**
     * The thread manager.
     */
    private ThreadManager threadManager;
    
    public JAMailboxFactory(ThreadManager threadManager) {
        this.threadManager = threadManager;
    }

    /**
     * Create a JAMailboxFactory
     *
     * @param threadCount The number of concurrent to be used.
     * @return A new JAMailboxFactory.
     */
    public static JAMailboxFactory newMailboxFactory(int threadCount) {
        return new JAMailboxFactory(JAThreadManager.newThreadManager(threadCount));
    }

    /**
     * Returns the thread manager.
     *
     * @return The thread manager.
     */
    public ThreadManager getThreadManager() {
        return threadManager;
    }

    /**
     * Stop all the threads as they complete their tasks.
     */
    public void close() {
        threadManager.close();
    }

    /**
     * Create a mailbox.
     *
     * @return A new mailbox.
     */
    @Override
    final public Mailbox createMailbox() {
        final JLPCMailbox mailbox = new JLPCMailbox(this);
        return mailbox;
    }

    /**
     * Create an asynchronous mailbox.
     *
     * @return A new asynchronous mailbox.
     */
    @Override
    final public Mailbox createAsyncMailbox() {
        final JLPCMailbox mailbox = new JLPCMailbox(this, true);
        return mailbox;
    }
}
