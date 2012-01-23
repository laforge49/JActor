package org.agilewiki.jactor.pubsub.timing;

import junit.framework.TestCase;
import org.agilewiki.jactor.*;
import org.agilewiki.jactor.parallel.JAParallel;
import org.agilewiki.jactor.pubsub.Subscribe;

public class SharedTimingTest extends TestCase {
    public void test() {

        //int c = 10;
        //int s = 1000;
        //int p = 1;
        //int t = 4;

        //int c = 50000;
        //int s = 1000;
        //int p = 4;
        //int t = 4;

        //4 parallel runs of 50000 requests sent to 1000 subscribers
        //publications per sec = 34470872
        //response time 116 nanoseconds

        //int c = 10000;
        //int s = 1000;
        //int p = 8;
        //int t = 4;

        //8 parallel runs of 10000 requests sent to 1000 subscribers
        //publications per sec = 29293299
        //response time 137 nanoseconds

        //int c = 1000000;
        //int s = 10;
        //int p = 8;
        //int t = 4;

        //8 parallel runs of 1000000 requests sent to 10 subscribers
        //publications per sec = 18939393
        //response time 211 nanoseconds

        int c = 10000;
        int s = 1000;
        int p = 16;
        int t = 4;

        //16 parallel runs of 10000 requests sent to 1000 subscribers
        //publications per sec = 31904287
        //response time 53 nanoseconds

        MailboxFactory mailboxFactory = JAMailboxFactory.newMailboxFactory(t);
        try {
            JAFuture future = new JAFuture();
            Actor[] drivers = new Actor[p];
            int i = 0;
            while (i < p) {
                Mailbox sharedMailbox = mailboxFactory.createAsyncMailbox();
                Driver1 driver = new Driver1(sharedMailbox);
                Actor pubsub = driver.pubsub;
                drivers[i] = driver;
                int j = 0;
                while (j < s) {
                    Actor subscriber = new NullSubscriber(sharedMailbox);
                    Subscribe subscribe = new Subscribe(subscriber);
                    future.send(pubsub, subscribe);
                    j += 1;
                }
                i += 1;
            }
            JAParallel parallel = new JAParallel(mailboxFactory.createMailbox(), drivers);
            Timing timing = new Timing(c, 1);
            future.send(parallel, timing);
            future.send(parallel, timing);
            long t0 = System.currentTimeMillis();
            future.send(parallel, timing);
            long t1 = System.currentTimeMillis();
            System.out.println("" + p + " parallel runs of " + c + " requests sent to " + s + " subscribers");
            if (t1 != t0)
                System.out.println("publications per sec = " + ((c * s * p) * 1000L / (t1 - t0)));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mailboxFactory.close();
        }
    }
}
