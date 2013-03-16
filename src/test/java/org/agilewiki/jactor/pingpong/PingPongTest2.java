package org.agilewiki.jactor.pingpong;

import org.agilewiki.jactor.JAMailboxFactory;
import org.agilewiki.jactor.MailboxFactory;

import junit.framework.TestCase;

/**
 * Tests the number of request/reply cycles per second.
 */
public class PingPongTest2 extends TestCase {
    /** How many repeats? */
    private static final int REPEATS = 10;

    public void testRequestReplyCycleSpeed() throws Exception {
        final MailboxFactory mailboxFactory = JAMailboxFactory.newMailboxFactory(2);

        int count = 0;
        double duration = 0.0d;
        for (int i = 1; i <= REPEATS; i++) {
            System.out.println("Run #" + i + "/" + REPEATS);
            final Pinger2 pinger = new Pinger2(mailboxFactory.createMailbox(),
                    "Pinger");
            final Ponger2 ponger = new Ponger2(mailboxFactory.createMailbox());
            final Pinger2.HammerResult2 result = pinger.hammer(ponger);
            count += result.pings();
            duration += result.duration();
            System.out.println(result);
        }
        System.out.println("Average Request/Reply Cycles/sec = "
                + (count / duration));

        mailboxFactory.close();
    }
}
