package org.agilewiki.jactor.factory.timing.test2;

import junit.framework.TestCase;
import org.agilewiki.jactor.JAMailboxFactory;
import org.agilewiki.jactor.Mailbox;
import org.agilewiki.jactor.MailboxFactory;
import org.agilewiki.jactor.factory.timing.A;

public class Creation2Test extends TestCase {
    public void test() {

        long c = 1;

        //System.out.println("####################################################");
        //long c = 1000000000;
        //iterations per second = 2,801,120,448

        MailboxFactory mailboxFactory = JAMailboxFactory.newMailboxFactory(1);
        try {
            Mailbox m = mailboxFactory.createMailbox();
            loop(c, m);
            loop(c, m);
            long t0 = System.currentTimeMillis();
            loop(c, m);
            long t1 = System.currentTimeMillis();
            long d = t1 - t0;
            if (d > 0)
                System.out.println(1000 * c / d);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mailboxFactory.close();
        }
    }

    void loop(long c, Mailbox m)
            throws Exception {
        int i = 0;
        while (i < c) {
            (new A()).initialize(m);
            i += 1;
        }
    }
}
