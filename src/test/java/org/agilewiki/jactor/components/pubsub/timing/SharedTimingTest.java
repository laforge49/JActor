package org.agilewiki.jactor.components.pubsub.timing;

import junit.framework.TestCase;
import org.agilewiki.jactor.*;
import org.agilewiki.jactor.components.Include;
import org.agilewiki.jactor.components.JCActor;
import org.agilewiki.jactor.components.pubsub.Subscribe;
import org.agilewiki.jactor.parallel.JAParallel;

public class SharedTimingTest extends TestCase {
    public void test() {

        int c = 10;
        int b = 1000;
        int p = 1;
        int t = 4;

        //int c = 10000;
        //int b = 1000;
        //int p = 4;
        //int t = 4;

        //4 parallel runs of 10000 requests sent to 1000 subscribers
        //publications per sec = 72202166
        //response time 55 nanoseconds

        //int c = 10000;
        //int b = 1000;
        //int p = 8;
        //int t = 4;

        //8 parallel runs of 10000 requests sent to 1000 subscribers
        //publications per sec = 81799591
        //response time 49 nanoseconds

        //int c = 10000;
        //int b = 1000;
        //int p = 16;
        //int t = 4;

        //16 parallel runs of 10000 requests sent to 1000 subscribers
        //publications per sec = 75721722
        //response time 53 nanoseconds

        MailboxFactory mailboxFactory = JAMailboxFactory.newMailboxFactory(t);
        try {
            JAFuture future = new JAFuture();
            Actor[] drivers = new Actor[p];
            int i = 0;
            while (i < p) {
                Mailbox sharedMailbox = mailboxFactory.createAsyncMailbox();
                Actor driver = new JCActor(sharedMailbox);
                future.send(driver, new Include(Driver.class));
                drivers[i] = driver;
                int j = 0;
                while (j < b) {
                    Actor subscriber = new NullSubscriber(sharedMailbox);
                    Subscribe subscribe = new Subscribe(subscriber);
                    future.send(driver, subscribe);
                    j += 1;
                }
                i += 1;
            }
            JAParallel parallel = new JAParallel(mailboxFactory.createMailbox(), drivers);
            Timing timing = new Timing(c);
            future.send(parallel, timing);
            future.send(parallel, timing);
            long t0 = System.currentTimeMillis();
            future.send(parallel, timing);
            long t1 = System.currentTimeMillis();
            System.out.println("" + p + " parallel runs of " + c + " requests sent to " + b + " subscribers");
            if (t1 != t0)
                System.out.println("publications per sec = " + ((c * b * p) * 1000L / (t1 - t0)));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mailboxFactory.close();
        }
    }
}
