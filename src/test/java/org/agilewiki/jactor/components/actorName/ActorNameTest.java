package org.agilewiki.jactor.components.actorName;

import junit.framework.TestCase;
import org.agilewiki.jactor.JAFuture;
import org.agilewiki.jactor.JAMailboxFactory;
import org.agilewiki.jactor.MailboxFactory;
import org.agilewiki.jactor.components.Include;
import org.agilewiki.jactor.components.JCActor;

public class ActorNameTest extends TestCase {
    public void test() {
        MailboxFactory mailboxFactory = JAMailboxFactory.newMailboxFactory(1);
        try {
            JAFuture future = new JAFuture();
            JCActor a = new JCActor(mailboxFactory.createMailbox());
            future.call(a, new Include(ActorName.class));
            System.err.println(future.send(a, new GetActorName()));
            future.send(a, new SetActorName("foo"));
            System.err.println(future.send(a, new GetActorName()));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mailboxFactory.close();
        }
    }
}
