package org.agilewiki.jactor.components.pubsubComponent;

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
            future.send(publisher, new Include(PubSub.class));
            Actor subscriber1 = new Subscriber(mailbox);
            Actor subscriber2 = new Subscriber(mailbox);
            future.send(publisher, new Subscribe(subscriber1));
            future.send(publisher, new Subscribe(subscriber2));
            future.send(publisher, new Publish(new PSRequest()));
            future.send(publisher, new Unsubscribe(subscriber1));
            future.send(publisher, new Publish(new PSRequest()));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mailboxFactory.close();
        }
    }
}
