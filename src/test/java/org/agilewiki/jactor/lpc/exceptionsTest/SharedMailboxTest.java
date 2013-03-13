package org.agilewiki.jactor.lpc.exceptionsTest;

import junit.framework.TestCase;
import org.agilewiki.jactor.JAFuture;
import org.agilewiki.jactor.JAMailboxFactory;
import org.agilewiki.jactor.Mailbox;
import org.agilewiki.jactor.MailboxFactory;

/**
 * Test code.
 */
public class SharedMailboxTest extends TestCase {
    public void testExceptions()
            throws Exception {
        MailboxFactory mailboxFactory = JAMailboxFactory.newMailboxFactory(1);
        Mailbox sharedMailbox = mailboxFactory.createMailbox();
        try {
            Doer doer = new Doer();
            doer.initialize(sharedMailbox);
            Driver driver = new Driver();
            driver.initialize(sharedMailbox);
            driver.doer = doer;
            JAFuture future = new JAFuture();
            try {
                System.out.println("Test Go1");
                Go1.req.send(future, driver);
            } catch (Throwable e) {
                System.out.println("Go1: " + e.getMessage());
            }
            try {
                System.out.println("Test Go2");
                Go2.req.send(future, driver);
            } catch (Throwable e) {
                System.out.println("Go2: " + e.getMessage());
            }
        } finally {
            mailboxFactory.close();
        }
    }
}
