package org.agilewiki.jactor.lpc;

import org.agilewiki.jactor.concurrent.JAThreadFactory;
import org.agilewiki.jactor.concurrent.JAThreadManager;
import org.agilewiki.jactor.concurrent.ThreadManager;

import java.util.concurrent.ThreadFactory;

/**
 * Implements MailboxFactory.
 */
public class JMailboxFactory implements MailboxFactory {

    /**
     * The thread manager.
     */
    private ThreadManager threadManager;
    
    public JMailboxFactory(ThreadManager threadManager) {
        this.threadManager = threadManager;
    }

    /**
     * Create a JMailboxFactory
     *
     * @param threadCount The number of concurrent to be used.
     * @return A new JMailboxFactory.
     */
    public static JMailboxFactory newMailboxFactory(int threadCount) {
        return new JMailboxFactory(JAThreadManager.newThreadManager(threadCount));
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
