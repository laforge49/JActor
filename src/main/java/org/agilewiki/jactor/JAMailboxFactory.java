/*
 * Copyright 2011 Bill La Forge
 *
 * This file is part of AgileWiki and is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License (LGPL) as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This code is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA
 * or navigate to the following url http://www.gnu.org/licenses/lgpl-2.1.txt
 *
 * Note however that only Scala, Java and JavaScript files are being covered by LGPL.
 * All other files are covered by the Common Public License (CPL).
 * A copy of this license is also included and can be
 * found as well at http://www.opensource.org/licenses/cpl1.0.txt
 */
package org.agilewiki.jactor;

import org.agilewiki.jactor.concurrent.JAThreadManager;
import org.agilewiki.jactor.concurrent.ThreadManager;
import org.agilewiki.jactor.lpc.JLPCMailbox;
import org.agilewiki.jactor.lpc.Request;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;

/**
 * <p>
 * Implements MailboxFactory.
 * In general you need only one instance of MailboxFactory per program.
 * </p>
 * <pre>
 *         MailboxFactory mailboxFactory = JAMailboxFactory.newMailboxFactory(1);
 *         try {
 *             ...
 *         } finally {
 *             mailboxFactory.close();
 *         }
 * </pre>
 */
public class JAMailboxFactory implements MailboxFactory {
    private List<Closable> closables = new ArrayList<Closable>();
    private Timer timer = null;

    @Override
    public Timer timer() throws Exception {
        if (timer == null) {
            timer = new Timer();
        }
        return timer;
    }

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

    @Override
    public boolean addClosable(Closable closable) {
        if (closable == null)
            throw new IllegalArgumentException("may not be null");
        return closables.add(closable);
    }

    @Override
    public boolean removeClosable(Closable closable) {
        return closables.remove(closable);
    }

    /**
     * Stop all the threads as they complete their tasks.
     */
    @Override
    public void close() {
        List<Closable> c = new ArrayList<Closable>(closables);
        Iterator<Closable> it = c.iterator();
        while (it.hasNext())
            it.next().close();
        if (timer != null)
            timer.cancel();
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

    @Override
    public void eventException(Request request, Exception exception) {
        logException(false, request.getClass().getName() +
                " event exception: ", exception);
    }

    @Override
    public void logException(boolean fatal, String msg, Exception exception) {
        threadManager.logException(fatal, msg, exception);
    }
}
