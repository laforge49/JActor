package org.agilewiki.jactor.events.quadTest;

import junit.framework.TestCase;
import org.agilewiki.jactor.concurrent.JAThreadManager;
import org.agilewiki.jactor.concurrent.ThreadManager;
import org.agilewiki.jactor.events.Echo;
import org.agilewiki.jactor.events.JAEventFuture;

/**
 * Test code.
 */
final public class QuadTest extends TestCase {
    public void testTiming() {
        ThreadManager threadManager = JAThreadManager.newThreadManager(8);
        try {
            Driver driver = new Driver(threadManager, 10, new Echo(threadManager));
            JAEventFuture<Object> eventFuture = new JAEventFuture<Object>();
            int i = 0;
            while (i < 10) {
                eventFuture.send(driver, eventFuture);
                i += 1;
            }
        } finally {
            threadManager.close();
        }
    }
}
