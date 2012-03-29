package org.agilewiki.jactor.lpc.creation.test5;

import junit.framework.TestCase;
import org.agilewiki.jactor.JAMailboxFactory;
import org.agilewiki.jactor.Mailbox;
import org.agilewiki.jactor.MailboxFactory;
import org.agilewiki.jactor.bind.Open;
import org.agilewiki.jactor.components.Include;
import org.agilewiki.jactor.components.JCActor;
import org.agilewiki.jactor.components.factory.Factory;
import org.agilewiki.jactor.factory.ActorFactory;
import org.agilewiki.jactor.factory.GetActorFactory;
import org.agilewiki.jactor.factory.RegisterActorFactory;
import org.agilewiki.jactor.lpc.creation.AFactory;

public class Creation5Test extends TestCase {
    public void test() {
        //System.out.println("####################################################");

        long c = 1;

        //long c = 1000000000;
        //iterations per second = 1,187,648,546

        MailboxFactory mailboxFactory = JAMailboxFactory.newMailboxFactory(1);
        try {
            JCActor f = new JCActor(mailboxFactory.createMailbox());
            (new Include(Factory.class)).call(f);
            (new RegisterActorFactory(new AFactory("A"))).call(f);
            Open.req.call(f);
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

    void loop(long c, JCActor f) throws Exception {
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
