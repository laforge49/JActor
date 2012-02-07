package org.agilewiki.jactor.components.pubsub;

import junit.framework.TestCase;
import org.agilewiki.jactor.*;
import org.agilewiki.jactor.components.Include;
import org.agilewiki.jactor.components.JCActor;

public class PubSubTest extends TestCase {
    public void test() {
        MailboxFactory mailboxFactory = JAMailboxFactory.newMailboxFactory(1);
        try {
            Mailbox mailbox = mailboxFactory.createMailbox();
            Actor publisher = new JCActor(mailbox);
            JAFuture future = new JAFuture();
            (new Include(PubSub.class)).call(publisher);
            Actor subscriber1 = new Subscriber(mailbox);
            Actor subscriber2 = new Subscriber(mailbox);
            (new Subscribe(subscriber1)).call(publisher);
            (new Subscribe(subscriber2)).call(publisher);
            (new Publish(new PSRequest())).send(future, publisher);
            (new Unsubscribe(subscriber1)).call(publisher);
            (new Publish(new PSRequest())).send(future, publisher);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mailboxFactory.close();
        }
    }
}
