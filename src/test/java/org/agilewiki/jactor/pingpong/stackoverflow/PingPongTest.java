package org.agilewiki.jactor.pingpong.stackoverflow;

import org.agilewiki.jactor.JAMailboxFactory;
import org.agilewiki.jactor.MailboxFactory;

import junit.framework.TestCase;

/**
 * Tests the number of request/reply cycles per second.
 */
public class PingPongTest extends TestCase {
    /** How many repeats? */
    private static final int REPEATS = 10;

    public void testRequestReplyCycleSpeed() throws Exception {
        final MailboxFactory mailboxFactory = JAMailboxFactory.newMailboxFactory(2);

        int count = 0;
        double duration = 0.0d;
        for (int i = 1; i <= REPEATS; i++) {
            System.out.println("Run #" + i + "/" + REPEATS);
            final Pinger pinger = new Pinger(mailboxFactory.createMailbox(),
                    "Pinger");
            final Ponger ponger = new Ponger(mailboxFactory.createMailbox());
            final Pinger.HammerResult2 result = pinger.hammer(ponger);
            count += result.pings();
            duration += result.duration();
            System.out.println(result);
        }
        System.out.println("Average Request/Reply Cycles/sec = "
                + (count / duration));

        mailboxFactory.close();
    }
}
