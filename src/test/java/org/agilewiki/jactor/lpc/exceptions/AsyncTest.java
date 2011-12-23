package org.agilewiki.jactor.lpc.exceptions;

import junit.framework.TestCase;
import org.agilewiki.jactor.lpc.*;

public class AsyncTest extends TestCase {
    public void testExceptions() {
        MailboxFactory mailboxFactory = JMailboxFactory.newMailboxFactory(1);
        LPCMailbox doerMailbox = mailboxFactory.createAsyncMailbox();
        LPCMailbox driverMailbox = mailboxFactory.createAsyncMailbox();
        try {
            LPCActor doer = new Doer(doerMailbox);
            LPCActor driver = new Driver(driverMailbox, doer);
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
