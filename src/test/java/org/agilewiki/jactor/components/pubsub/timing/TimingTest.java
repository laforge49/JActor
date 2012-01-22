package org.agilewiki.jactor.components.pubsub.timing;

import junit.framework.TestCase;
import org.agilewiki.jactor.*;
import org.agilewiki.jactor.components.Include;
import org.agilewiki.jactor.components.JCActor;
import org.agilewiki.jactor.components.pubsub.Subscribe;
import org.agilewiki.jactor.parallel.JAParallel;

public class TimingTest extends TestCase {
    public void test() {

        int c = 10;
        int b = 1;
        int s = 1000;
        int p = 1;
        int t = 4;

        MailboxFactory mailboxFactory = JAMailboxFactory.newMailboxFactory(t);
        try {
            JAFuture future = new JAFuture();
            Actor[] drivers = new Actor[p];
            int i = 0;
            while (i < p) {
                Actor driver = new JCActor(mailboxFactory.createAsyncMailbox());
                future.send(driver, new Include(Driver.class));
                drivers[i] = driver;
                int j = 0;
                while (j < s) {
                    Actor subscriber = new NullSubscriber(mailboxFactory.createMailbox());
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
