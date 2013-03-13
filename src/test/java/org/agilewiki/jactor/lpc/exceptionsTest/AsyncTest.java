package org.agilewiki.jactor.lpc.exceptionsTest;

import junit.framework.TestCase;
import org.agilewiki.jactor.JAFuture;
import org.agilewiki.jactor.JAMailboxFactory;
import org.agilewiki.jactor.Mailbox;
import org.agilewiki.jactor.MailboxFactory;

/**
 * Test code.
 */
public class AsyncTest extends TestCase {
    public void testExceptions()
            throws Exception {
        MailboxFactory mailboxFactory = JAMailboxFactory.newMailboxFactory(1);
        Mailbox doerMailbox = mailboxFactory.createAsyncMailbox();
        Mailbox driverMailbox = mailboxFactory.createAsyncMailbox();
        try {
            Doer doer = new Doer();
            doer.initialize(doerMailbox);
            Driver driver = new Driver();
            driver.initialize(driverMailbox);
            driver.doer = doer;
            JAFuture future = new JAFuture();
            try {
                Go1.req.send(future, driver);
            } catch (Throwable e) {
                System.out.println("Go1: " + e.getMessage());
            }
            try {
                Go2.req.send(future, driver);
            } catch (Throwable e) {
                System.out.println("Go2: " + e.getMessage());
            }
        } finally {
            mailboxFactory.close();
        }
    }
}
