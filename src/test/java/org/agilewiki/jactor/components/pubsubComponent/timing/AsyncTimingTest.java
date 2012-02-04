package org.agilewiki.jactor.components.pubsubComponent.timing;

import junit.framework.TestCase;
import org.agilewiki.jactor.Actor;
import org.agilewiki.jactor.JAFuture;
import org.agilewiki.jactor.JAMailboxFactory;
import org.agilewiki.jactor.MailboxFactory;
import org.agilewiki.jactor.components.Include;
import org.agilewiki.jactor.components.JCActor;
import org.agilewiki.jactor.components.pubsubComponent.Subscribe;
import org.agilewiki.jactor.parallel.JAParallel;

public class AsyncTimingTest extends TestCase {
    public void test() {

        int c = 1;
        int b = 1;
        int s = 10;
        int p = 4;
        int t = 4;

        //int c = 100000;
        //int b = 1;
        //int s = 10;
        //int p = 4;
        //int t = 4;
        //4 parallel runs of 100000 bursts of 1 requests sent to 10 subscribers
        //publications per sec = 2751031
        //response time 1.5 microseconds

        //int c = 1000;
        //int b = 1;
        //int s = 1000;
        //int p = 4;
        //int t = 4;
        //4 parallel runs of 1000 bursts of 1 requests sent to 1000 subscribers
        //publications per sec = 1642036
        //response time 2.4 microseconds

        //int c = 1000;
        //int b = 1000;
        //int s = 10;
        //int p = 4;
        //int t = 4;
        //4 parallel runs of 1000 bursts of 1000 requests sent to 10 subscribers
        //publications per sec = 11305822

        MailboxFactory mailboxFactory = JAMailboxFactory.newMailboxFactory(t);
        try {
            JAFuture future = new JAFuture();
            Actor[] drivers = new Actor[p];
            int i = 0;
            while (i < p) {
                Actor driver = new JCActor(mailboxFactory.createAsyncMailbox());
                driver.setInitialBufferCapacity(b + 10);
                future.call(driver, new Include(Driver.class));
                drivers[i] = driver;
                int j = 0;
                while (j < s) {
                    Actor subscriber = new NullSubscriber(mailboxFactory.createAsyncMailbox());
                    subscriber.setInitialBufferCapacity(b + 10);
                    Subscribe subscribe = new Subscribe(subscriber);
                    future.send(driver, subscribe);
                    j += 1;
                }
                i += 1;
            }
            JAParallel parallel = new JAParallel(mailboxFactory.createMailbox(), drivers);
            Timing timing = new Timing(c, b);
            future.send(parallel, timing);
            future.send(parallel, timing);
            long t0 = System.currentTimeMillis();
            future.send(parallel, timing);
            long t1 = System.currentTimeMillis();
            System.out.println("" + p + " parallel runs of " + c + " bursts of " + b + " requests sent to " + s + " subscribers");
            if (t1 != t0)
                System.out.println("publications per sec = " + (1000L * c * b * s * p / (t1 - t0)));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mailboxFactory.close();
        }
    }
}
