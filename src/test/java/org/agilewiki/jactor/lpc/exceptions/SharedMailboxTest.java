package org.agilewiki.jactor.lpc.exceptions;

import junit.framework.TestCase;
import org.agilewiki.jactor.concurrent.JAThreadManager;
import org.agilewiki.jactor.concurrent.ThreadManager;
import org.agilewiki.jactor.lpc.JLPCFuture;
import org.agilewiki.jactor.lpc.JLPCMailbox;
import org.agilewiki.jactor.lpc.LPCActor;
import org.agilewiki.jactor.lpc.LPCMailbox;

public class SharedMailboxTest extends TestCase {
    public void testExceptions() {
        ThreadManager threadManager = JAThreadManager.newThreadManager(1);
        LPCMailbox sharedMailbox = new JLPCMailbox(threadManager, false);
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
            threadManager.close();
        }
    }
}
