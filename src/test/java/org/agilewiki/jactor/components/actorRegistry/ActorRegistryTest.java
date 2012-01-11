package org.agilewiki.jactor.components.actorRegistry;

import junit.framework.TestCase;
import org.agilewiki.jactor.JAFuture;
import org.agilewiki.jactor.JAMailboxFactory;
import org.agilewiki.jactor.MailboxFactory;
import org.agilewiki.jactor.components.actorName.ActorName;
import org.agilewiki.jactor.components.actorName.GetActorName;
import org.agilewiki.jactor.components.actorName.SetActorName;
import org.agilewiki.jactor.composite.Include;
import org.agilewiki.jactor.composite.JCActor;

public class ActorRegistryTest extends TestCase {
    public void test() {
        MailboxFactory mailboxFactory = JAMailboxFactory.newMailboxFactory(1);
        try {
            JAFuture future = new JAFuture();
            JCActor a = new JCActor(mailboxFactory.createMailbox());
            future.send(a, new Include(ActorName.class));
            future.send(a, new SetActorName("foo"));
            JCActor r = new JCActor(mailboxFactory.createMailbox());
            future.send(r, new Include(ActorRegistry.class));
            future.send(r, new RegisterActor(a));
            System.err.println(future.send(r, new GetRegisteredActor("abe")));
            System.err.println(future.send(r, new GetRegisteredActor("foo")));
            future.send(r, new UnregisterActor("foo"));
            System.err.println(future.send(r, new GetRegisteredActor("foo")));
            r.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mailboxFactory.close();
        }
    }
}
