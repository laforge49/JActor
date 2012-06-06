package org.agilewiki.jactor.basics;

import junit.framework.TestCase;
import org.agilewiki.jactor.JAFuture;
import org.agilewiki.jactor.JAMailboxFactory;
import org.agilewiki.jactor.Mailbox;
import org.agilewiki.jactor.MailboxFactory;

/**
 * Test code.
 */
public class Test5 extends TestCase {
    public void test() throws Exception {
        MailboxFactory mailboxFactory = JAMailboxFactory.newMailboxFactory(10);
        Mailbox mailbox = mailboxFactory.createMailbox();
        Actor5a actor5a = new Actor5a();
        actor5a.initialize(mailbox);
        JAFuture future = new JAFuture();
        boolean response = Parallel.req.send(future, actor5a);
        assertEquals(true, response);
        mailboxFactory.close();
    }
}
