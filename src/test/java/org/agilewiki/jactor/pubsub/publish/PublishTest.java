package org.agilewiki.jactor.pubsub.publish;

import junit.framework.TestCase;
import org.agilewiki.jactor.*;
import org.agilewiki.jactor.lpc.JLPCActor;
import org.agilewiki.jactor.lpc.Request;
import org.agilewiki.jactor.pubsub.publisher.*;
import org.agilewiki.jactor.pubsub.subscriber.JASubscriber;
import org.agilewiki.jactor.pubsub.subscriber.Subscriber;

/**
 * Test code.
 */
public class PublishTest extends TestCase {
    public void test() {
        MailboxFactory mailboxFactory = JAMailboxFactory.newMailboxFactory(1);
        try {
            JAFuture future = new JAFuture();
            Mailbox mailbox = mailboxFactory.createMailbox();
            Req req = new Req();
            Publish publish = new Publish(req);
            int c = 0;
            Sub s = new Sub();
            s.initialize(mailbox);
            s.setActorName("foo");
            JAPublisher p = new JAPublisher();
            p.initialize(mailbox);
            c = publish.send(future, p);
            assertEquals(0, c);
            (new Subscribe(s)).send(future, p);
            GetSubscriber getSubscriber = new GetSubscriber("foo");
            Subscriber s1 = getSubscriber.send(future, p);
            assertEquals(s, s1);
            c = publish.send(future, p);
            assertEquals(1, c);
            (new Unsubscribe(s)).send(future, p);
            c = publish.send(future, p);
            assertEquals(0, c);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mailboxFactory.close();
        }
    }
}

/**
 * Test code.
 */
class Sub extends JASubscriber {
    void req() {
    }
}

/**
 * Test code.
 */
class Req extends Request<Object, Sub> {

    @Override
    public boolean isTargetType(Actor targetActor) {
        return targetActor instanceof Sub;
    }

    @Override
    public void processRequest(JLPCActor targetActor, RP rp) throws Exception {
        ((Sub) targetActor).req();
        rp.processResponse(null);
    }
}