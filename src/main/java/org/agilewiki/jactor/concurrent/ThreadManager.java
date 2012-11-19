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
package org.agilewiki.jactor.concurrent;

import java.util.concurrent.ThreadFactory;

/**
 * A ThreadManager is used to process a collection of Runnable tasks.
 * ThreadManager is a thread pool, but with a simplified API and
 * assumes that the thread pool has a fixed number of threads.
 */
public interface ThreadManager {
    /**
     * Create and start the concurrent.
     *
     * @param threadCount   The number of concurrent to be used.
     * @param threadFactory Used to create the concurrent.
     */
    public void start(int threadCount, ThreadFactory threadFactory);

    /**
     * Begin running a task.
     *
     * @param runnable The run method is to be called by another thread.
     */
    public void process(Runnable runnable);

    /**
     * Stop all the threads as they complete their tasks.
     */
    public void close();

    public void logException(boolean fatal, String msg, Exception exception);
}
