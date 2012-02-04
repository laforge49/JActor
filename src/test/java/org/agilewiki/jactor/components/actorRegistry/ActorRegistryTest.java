package org.agilewiki.jactor.components.actorRegistry;

import junit.framework.TestCase;
import org.agilewiki.jactor.JAFuture;
import org.agilewiki.jactor.JAMailboxFactory;
import org.agilewiki.jactor.MailboxFactory;
import org.agilewiki.jactor.components.Include;
import org.agilewiki.jactor.components.JCActor;
import org.agilewiki.jactor.components.actorName.ActorName;
import org.agilewiki.jactor.components.actorName.SetActorName;

public class ActorRegistryTest extends TestCase {
    public void test1() {
        System.err.println("test1");
        MailboxFactory mailboxFactory = JAMailboxFactory.newMailboxFactory(1);
        try {
            JAFuture future = new JAFuture();
            JCActor a = new JCActor(mailboxFactory.createMailbox());
            future.call(a, new Include(ActorName.class));
            future.call(a, new SetActorName("foo"));
            JCActor r = new JCActor(mailboxFactory.createMailbox());
            future.call(r, new Include(ActorRegistry.class));
            future.call(r, new RegisterActor(a));
            System.err.println(future.call(r, new GetRegisteredActor("abe")));
            System.err.println(future.call(r, new GetRegisteredActor("foo")));
            future.call(r, new UnregisterActor("foo"));
            System.err.println(future.call(r, new GetRegisteredActor("foo")));
            r.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mailboxFactory.close();
        }
    }

    public void test2() {
        System.err.println("test2");
        MailboxFactory mailboxFactory = JAMailboxFactory.newMailboxFactory(1);
        try {
            JAFuture future = new JAFuture();
            JCActor pr = new JCActor(mailboxFactory.createMailbox());
            future.call(pr, new Include(ActorRegistry.class));
            JCActor a = new JCActor(mailboxFactory.createMailbox());
            future.call(a, new Include(ActorName.class));
            future.call(a, new SetActorName("foo"));
            future.call(pr, new RegisterActor(a));
            JCActor r = new JCActor(mailboxFactory.createMailbox());
            r.setParent(pr);
            future.call(r, new Include(ActorRegistry.class));
            System.err.println(future.call(r, new GetRegisteredActor("abe")));
            System.err.println(future.call(r, new GetRegisteredActor("foo")));
            future.call(r, new UnregisterActor("foo"));
            System.err.println(future.call(r, new GetRegisteredActor("foo")));
            future.call(pr, new UnregisterActor("foo"));
            System.err.println(future.call(r, new GetRegisteredActor("foo")));
            r.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mailboxFactory.close();
        }
    }
}
