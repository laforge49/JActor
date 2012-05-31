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
                System.out.println("Test Go1");
                Go1.req.send(future, driver);
            } catch (Exception e) {
                System.out.println("Go1: " + e.getMessage());
            }
            try {
                System.out.println("Test Go2");
                Go2.req.send(future, driver);
            } catch (Exception e) {
                System.out.println("Go2: " + e.getMessage());
            }
        } finally {
            mailboxFactory.close();
        }
    }
}
