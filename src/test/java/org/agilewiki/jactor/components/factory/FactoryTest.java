package org.agilewiki.jactor.components.factory;

import junit.framework.TestCase;
import org.agilewiki.jactor.JAMailboxFactory;
import org.agilewiki.jactor.MailboxFactory;
import org.agilewiki.jactor.components.Include;
import org.agilewiki.jactor.components.JCActor;
import org.agilewiki.jactor.components.actorRegistry.ActorRegistry;

public class FactoryTest extends TestCase {
    public void test1() {
        System.err.println("test1");
        MailboxFactory mailboxFactory = JAMailboxFactory.newMailboxFactory(1);
        try {
            JCActor f = new JCActor(mailboxFactory.createMailbox());
            (new Include(Factory.class)).call(f);
            (new Include(ActorRegistry.class)).call(f);
            (new DefineActorType("Foo", Foo.class)).call(f);
            JCActor a = (new NewActor("Foo")).call(f);
            (new Hi()).call(a);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mailboxFactory.close();
        }
    }

    public void test2() {
        System.err.println("test2");
        MailboxFactory mailboxFactory = JAMailboxFactory.newMailboxFactory(1);
        JCActor f = null;
        try {
            f = new JCActor(mailboxFactory.createMailbox());
            (new Include(Factory.class)).call(f);
            (new Include(ActorRegistry.class)).call(f);
            (new DefineActorType("Bar", Bar.class)).call(f);
            JCActor a = (new NewActor("Bar", null, "Bloop")).call(f);
            (new Hi()).call(a);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            f.close();
            mailboxFactory.close();
        }
    }
}
