package org.agilewiki.jactor.lpc.timing;

import junit.framework.TestCase;
import org.agilewiki.jactor.concurrent.JAThreadManager;
import org.agilewiki.jactor.concurrent.ThreadManager;
import org.agilewiki.jactor.lpc.JLPCFuture;
import org.agilewiki.jactor.lpc.JLPCMailbox;
import org.agilewiki.jactor.lpc.LPCActor;
import org.agilewiki.jactor.lpc.LPCMailbox;

public class AsyncMailboxTest extends TestCase {
    public void testTiming() {
        int c = 2;
        int b = 3;
        int p = 1;
        int t = 1;

        ThreadManager threadManager = JAThreadManager.newThreadManager(t);
        try {
            LPCActor[] senders = new LPCActor[p];
            int i = 0;
            while (i < p) {
                LPCMailbox echoMailbox = new JLPCMailbox(threadManager, true);
                LPCActor echo = new Echo(echoMailbox);
                echo.setInitialBufferCapacity(b + 10);
                LPCMailbox senderMailbox = new JLPCMailbox(threadManager, true);
                if (b == 1) senders[i] = new Sender1(senderMailbox, echo, c, b);
                else senders[i] = new Sender(senderMailbox, echo, c, b);
                senders[i].setInitialBufferCapacity(b + 10);
                i += 1;
            }
            Driver driver = new Driver(new JLPCMailbox(threadManager), senders, p);
            JLPCFuture future = new JLPCFuture();
            future.send(driver, future);
            future.send(driver, future);
            long t0 = System.currentTimeMillis();
            future.send(driver, future);
            long t1 = System.currentTimeMillis();
            System.out.println("" + p + " parallel runs of " + (2L * c * b) + " messages each.");
            System.out.println("" + (2L * c * b * p) + " messages sent with " + t + " threads.");
            if (t1 != t0)
                System.out.println("msgs per sec = " + ((2L * c * b * p) * 1000L / (t1 - t0)));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            threadManager.close();
        }
    }
}
