package org.agilewiki.jactor.pubsub.latency;

import junit.framework.TestCase;
import org.agilewiki.jactor.JAFuture;
import org.agilewiki.jactor.JAMailboxFactory;
import org.agilewiki.jactor.Mailbox;
import org.agilewiki.jactor.MailboxFactory;
import org.agilewiki.jactor.pubsub.publisher.JAPublisher;

/**
 * Test code.
 */
public class SharedTest extends TestCase {
    public void test() throws Exception {

        int r = 1;
        int s = 1;

        //int r = 10000;
        //int s = 10000;
        //mps = 19857029


        MailboxFactory mailboxFactory = JAMailboxFactory.newMailboxFactory(1);
        Mailbox mailbox = mailboxFactory.createMailbox();
        Driver driver = new Driver();
        driver.initialize(mailbox);
        driver.r = r;
        driver.s = s;
        JAFuture future = new JAFuture();
        JAPublisher pub = new JAPublisher();
        pub.initialize(mailbox);
        driver.pub = pub;
        Go.req.send(future, driver);
        pub = new JAPublisher();
        pub.initialize(mailbox);
        driver.pub = pub;
        Go.req.send(future, driver);
        pub = new JAPublisher();
        pub.initialize(mailbox);
        driver.pub = pub;
        long t0 = System.currentTimeMillis();
        Go.req.send(future, driver);
        long t1 = System.currentTimeMillis();
        long t = t1 - t0;
        long tm = r * s;
        if (t > 0L) {
            System.out.println("messages: " + tm);
            System.out.println("milliseconds: " + t);
            System.out.println("messages per second: " + (1000L * tm / t));
        }
        mailboxFactory.close();
    }
}
