package org.agilewiki.jactor.lpc.exceptionsTest;

import junit.framework.TestCase;
import org.agilewiki.jactor.*;

/**
 * Test code.
 */
public class AsyncTest extends TestCase {
    public void testExceptions() {
        MailboxFactory mailboxFactory = JAMailboxFactory.newMailboxFactory(1);
        Mailbox doerMailbox = mailboxFactory.createAsyncMailbox();
        Mailbox driverMailbox = mailboxFactory.createAsyncMailbox();
        try {
            Actor doer = new Doer(doerMailbox);
            Actor driver = new Driver(driverMailbox, doer);
            JAFuture future = new JAFuture();
            try {
                System.out.println("Test T1");
                future.send(driver, new T1());
            } catch (Exception e) {
                System.out.println("T1: " + e.getMessage());
            }
            try {
                System.out.println("Test T2");
                future.send(driver, new T2());
            } catch (Exception e) {
                System.out.println("T2: " + e.getMessage());
            }
        } finally {
            mailboxFactory.close();
        }
    }
}
