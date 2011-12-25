package org.agilewiki.jactor.lpc.exceptions;

import junit.framework.TestCase;
import org.agilewiki.jactor.*;

public class SharedMailboxTest extends TestCase {
    public void testExceptions() {
        MailboxFactory mailboxFactory = JAMailboxFactory.newMailboxFactory(1);
        Mailbox sharedMailbox = mailboxFactory.createMailbox();
        try {
            Actor doer = new Doer(sharedMailbox);
            Actor driver = new Driver(sharedMailbox, doer);
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
