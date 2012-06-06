package org.agilewiki.jactor.counterTest;

import junit.framework.TestCase;
import org.agilewiki.jactor.*;

/**
 * Test code.
 */
public class CounterTest extends TestCase {
    public void testShared() throws Exception {

        long runs = 10;

        //long runs = 1000000000;
        //[java-shared] Number of runs: 1000000000
        //[java-shared] Count: 100000000000
        //[java-shared] Test time in seconds: 10.457
        //[java-shared] Messages per second: 9.562972171750979E7
        MailboxFactory mailboxFactory = JAMailboxFactory.newMailboxFactory(10);
        try {
            Mailbox sharedMailbox = mailboxFactory.createMailbox();
            CounterActor counterActor = new CounterActor();
            counterActor.initialize(sharedMailbox);
            Driver driver = new Driver();
            driver.initialize(sharedMailbox, counterActor, runs);
            JAFuture future = new JAFuture();
            long start = System.currentTimeMillis();
            Long count = (Long) SimpleRequest.req.send(future, driver);
            long finish = System.currentTimeMillis();
            double elapsedTime = (finish - start) / 1000.;
            System.out.println("[java-shared] Number of runs: " + runs);
            System.out.println("[java-shared] Count: " + count);
            System.out.println("[java-shared] Test time in seconds: " + elapsedTime);
            System.out.println("[java-shared] Messages per second: " + runs / elapsedTime);
        } finally {
            mailboxFactory.close();
        }
    }

    public void testUnshared() throws Exception {

        long runs = 10;

        //long runs = 1000000000;
        //[java-unshared] Number of runs: 1000000000
        //[java-unshared] Count: 100000000000
        //[java-unshared] Test time in seconds: 52.955
        //[java-unshared] Messages per second: 1.8883958077613067E7
        MailboxFactory mailboxFactory = JAMailboxFactory.newMailboxFactory(10);
        try {
            CounterActor counterActor = new CounterActor();
            counterActor.initialize(mailboxFactory.createMailbox());
            Driver driver = new Driver();
            driver.initialize(mailboxFactory.createMailbox(), counterActor, runs);
            JAFuture future = new JAFuture();
            long start = System.currentTimeMillis();
            Long count = (Long) SimpleRequest.req.send(future, driver);
            long finish = System.currentTimeMillis();
            double elapsedTime = (finish - start) / 1000.;
            System.out.println("[java-unshared] Number of runs: " + runs);
            System.out.println("[java-unshared] Count: " + count);
            System.out.println("[java-unshared] Test time in seconds: " + elapsedTime);
            System.out.println("[java-unshared] Messages per second: " + runs / elapsedTime);
        } finally {
            mailboxFactory.close();
        }
    }
}
