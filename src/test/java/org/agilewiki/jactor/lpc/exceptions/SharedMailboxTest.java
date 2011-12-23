package org.agilewiki.jactor.lpc.exceptions;

import junit.framework.TestCase;
import org.agilewiki.jactor.lpc.*;

public class SharedMailboxTest extends TestCase {
    public void testExceptions() {
        MailboxFactory mailboxFactory = JMailboxFactory.newMailboxFactory(1);
        LPCMailbox sharedMailbox = mailboxFactory.createMailbox();
        try {
            LPCActor doer = new Doer(sharedMailbox);
            LPCActor driver = new Driver(sharedMailbox, doer);
            JLPCFuture future = new JLPCFuture();
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
