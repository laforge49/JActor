package org.agilewiki.jactor.lpc.exceptionsTest;

import junit.framework.TestCase;
import org.agilewiki.jactor.*;

/**
 * Test code.
 */
public class SharedMailboxTest extends TestCase {
    public void testExceptions() {
        MailboxFactory mailboxFactory = JAMailboxFactory.newMailboxFactory(1);
        Mailbox sharedMailbox = mailboxFactory.createMailbox();
        try {
            Actor doer = new Doer(sharedMailbox);
            Actor driver = new Driver(sharedMailbox, doer);
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
