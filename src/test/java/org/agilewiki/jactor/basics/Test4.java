package org.agilewiki.jactor.basics;

import junit.framework.TestCase;
import org.agilewiki.jactor.JAFuture;
import org.agilewiki.jactor.JAMailboxFactory;
import org.agilewiki.jactor.Mailbox;
import org.agilewiki.jactor.MailboxFactory;

public class Test4 extends TestCase {
    public void test() throws Exception {
        MailboxFactory mailboxFactory = JAMailboxFactory.newMailboxFactory(10);
        Mailbox mailbox = mailboxFactory.createMailbox();
        Actor2 actor2 = new Actor2(mailbox);
        Actor4 actor4 = new Actor4(mailbox);
        actor4.setParent(actor2);
        JAFuture future = new JAFuture();
        boolean result = Validate1.req.send(future, actor4);
        assertFalse(result);
        Actor1 actor1 = new Actor1(mailbox);
        actor2.setParent(actor1);
        result = Validate1.req.send(future, actor4);
        assertTrue(result);
        mailboxFactory.close();
    }
}
