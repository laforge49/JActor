package org.agilewiki.jactor.basics;

import junit.framework.TestCase;
import org.agilewiki.jactor.JAFuture;
import org.agilewiki.jactor.JAMailboxFactory;
import org.agilewiki.jactor.Mailbox;
import org.agilewiki.jactor.MailboxFactory;

/**
 * Test code.
 */
public class Test4 extends TestCase {
    public void test() throws Exception {
        MailboxFactory mailboxFactory = JAMailboxFactory.newMailboxFactory(10);
        Mailbox mailbox = mailboxFactory.createMailbox();
        JAFuture future = new JAFuture();

        Actor2 actor2 = new Actor2();
        actor2.initialize(mailbox);
        Actor4 actor4 = new Actor4();
        actor4.initialize(mailbox, actor2);
        boolean result = Validate1.req.send(future, actor4);
        assertFalse(result);

        Actor1 actor1a = new Actor1();
        actor1a.initialize(mailbox);
        Actor2 actor2a = new Actor2();
        actor2a.initialize(mailbox, actor1a);
        Actor4 actor4a = new Actor4();
        actor4a.initialize(mailbox, actor2a);
        result = Validate1.req.send(future, actor4a);
        assertTrue(result);
        mailboxFactory.close();
    }
}
