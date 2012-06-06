package org.agilewiki.jactor.basics;

import junit.framework.TestCase;
import org.agilewiki.jactor.JAFuture;
import org.agilewiki.jactor.JAMailboxFactory;
import org.agilewiki.jactor.Mailbox;
import org.agilewiki.jactor.MailboxFactory;

/**
 * Test code.
 */
public class Test1 extends TestCase {
    public void test() throws Exception {
        MailboxFactory mailboxFactory = JAMailboxFactory.newMailboxFactory(10);
        Mailbox mailbox = mailboxFactory.createMailbox();
        Actor1 actor1 = new Actor1();
        actor1.initialize(mailbox);
        JAFuture future = new JAFuture();
        String result = Hi1.req.send(future, actor1);
        assertEquals("Hello world!", result);
        mailboxFactory.close();
    }
}
