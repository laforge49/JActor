package org.agilewiki.jactor.components.factory;

import junit.framework.TestCase;
import org.agilewiki.jactor.JAMailboxFactory;
import org.agilewiki.jactor.MailboxFactory;
import org.agilewiki.jactor.bind.Open;
import org.agilewiki.jactor.components.Include;
import org.agilewiki.jactor.components.JCActor;
import org.agilewiki.jactor.components.actorRegistry.ActorRegistry;

/**
 * Test code.
 */
public class FactoryTest extends TestCase {
    public void test() {
        MailboxFactory mailboxFactory = JAMailboxFactory.newMailboxFactory(1);
        try {
            JCActor f = new JCActor(mailboxFactory.createMailbox());
            (new Include(Factory.class)).call(f);
            (new Include(ActorRegistry.class)).call(f);
            (new DefineActorType("Foo", Foo.class)).call(f);
            Open.req.call(f);
            JCActor a = (JCActor) (new NewActor("Foo")).call(f);
            Open.req.call(a);
            (new Hi()).call(a);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mailboxFactory.close();
        }
    }
}
