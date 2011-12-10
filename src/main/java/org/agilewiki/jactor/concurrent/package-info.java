/**
 * <p>
 * The concurrent package contains a number of classes that are helpful
 * in delivering high-performance.
 * </p>
 * <p>
 * ConcurrentLinkedBlockingQueue builds on ConcurrentLinkedQueue, but adds a
 * take method which blocks on an semaphore when the queue is empty. It delivers
 * good performance, but only one thread can call take at a time.
 * </p>
 * <p>
 * JAThreadFactory is a light-weight implementation of ThreadFactory.
 * </p>
 * <p>
 * ThreadManager is an interface for a thread pool, but with a simplified API.
 * The assumption is that there are a fixed number of threads in the pool.
 * </p>
 * <p>
 * JAThreadManager is a high-performance implementation of ThreadManager.
 * </p>
 */
package org.agilewiki.jactor.concurrent;
