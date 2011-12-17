package org.agilewiki.jactor.apc.exceptions;

import junit.framework.TestCase;
import org.agilewiki.jactor.apc.JAPCFuture;
import org.agilewiki.jactor.concurrent.JAThreadManager;
import org.agilewiki.jactor.concurrent.ThreadManager;

public class APCExceptionsTest extends TestCase {
    public void testExceptions() {
        ThreadManager threadManager = JAThreadManager.newThreadManager(1);
        try {
            Driver driver = new Driver(threadManager);
            JAPCFuture eventFuture = new JAPCFuture();
            try {
                System.out.println("Test T1");
                eventFuture.send(driver, new T1());
            } catch (Exception e) {
                System.out.println("T1: "+e.getMessage());
            }
            /*
            try {
                System.out.println("Test T2");
                eventFuture.send(driver, new T2());
            } catch (Exception e) {
                System.out.println("T2: "+e.getMessage());
            }
            */
        } finally {
            threadManager.close();
        }
    }
}
