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

        //int r = 1;
        //int s = 1;
        //int m = 1;

        int r = 1000;
        int s = 100;
        int m = 1000;


        MailboxFactory mailboxFactory = JAMailboxFactory.newMailboxFactory(1);
        Mailbox mailbox = mailboxFactory.createMailbox();
        Driver driver = new Driver(mailbox);
        driver.r = r;
        driver.s = s;
        driver.m = m + 1;
        JAFuture future = new JAFuture();
        JAPublisher pub = new JAPublisher(mailbox);
        pub.setInitialBufferCapacity(1000);
        driver.pub = pub;
        Go.req.send(future, driver);
        pub = new JAPublisher(mailbox);
        pub.setInitialBufferCapacity(1000);
        driver.pub = pub;
        Go.req.send(future, driver);
        pub = new JAPublisher(mailbox);
        pub.setInitialBufferCapacity(1000);
        driver.pub = pub;
        long t0 = System.currentTimeMillis();
        Go.req.send(future, driver);
        long t1 = System.currentTimeMillis();
        driver.m = 1;
        pub = new JAPublisher(mailbox);
        pub.setInitialBufferCapacity(1000);
        driver.pub = pub;
        long t2 = System.currentTimeMillis();
        Go.req.send(future, driver);
        long t3 = System.currentTimeMillis();
        long t = (t1 - t0) - (t3 - t2);
        long tm = r * s * m;
        if (t > 0L) {
            System.out.println("messages: " + tm);
            System.out.println("milliseconds: " + t);
            System.out.println("messages per second: " + (1000L * tm / t));
        }
        mailboxFactory.close();
    }
}
