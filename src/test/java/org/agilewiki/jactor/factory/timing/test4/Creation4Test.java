package org.agilewiki.jactor.factory.timing.test4;

import junit.framework.TestCase;
import org.agilewiki.jactor.JAMailboxFactory;
import org.agilewiki.jactor.Mailbox;
import org.agilewiki.jactor.MailboxFactory;
import org.agilewiki.jactor.factory.ActorFactory;
import org.agilewiki.jactor.factory.GetActorFactory;
import org.agilewiki.jactor.factory.JAFactory;
import org.agilewiki.jactor.factory.timing.A;

public class Creation4Test extends TestCase {
    public void test() {
        //System.out.println("####################################################");

        long c = 1;

        //long c = 100000000;
        //iterations per second = 200,400,801 (was 142,045,454 when Factory component was used)

        MailboxFactory mailboxFactory = JAMailboxFactory.newMailboxFactory(1);
        try {
            JAFactory f = new JAFactory(mailboxFactory.createMailbox());
            f.defineActorType("A", A.class);
            loop(c, f);
            loop(c, f);
            long t0 = System.currentTimeMillis();
            loop(c, f);
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

    void loop(long c, JAFactory f) throws Exception {
        GetActorFactory gaf = new GetActorFactory("A");
        ActorFactory af = gaf.call(f);
        MailboxFactory mailboxFactory = f.getMailboxFactory();
        Mailbox m = mailboxFactory.createMailbox();
        int i = 0;
        while (i < c) {
            af.newActor(m, f);
            i += 1;
        }
    }
}
