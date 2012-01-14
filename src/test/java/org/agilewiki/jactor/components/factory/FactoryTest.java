package org.agilewiki.jactor.components.factory;

import junit.framework.TestCase;
import org.agilewiki.jactor.Actor;
import org.agilewiki.jactor.JAFuture;
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
            JAFuture future = new JAFuture();
            JCActor f = new JCActor(mailboxFactory.createMailbox());
            future.send(f, new Include(Factory.class));
            future.send(f, new Include(ActorRegistry.class));
            future.send(f, new DefineActorType("Foo", Foo.class));
            Actor a = (Actor) future.send(f, new NewActor("Foo"));
            future.send(a, new Hi());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mailboxFactory.close();
        }
    }
}
