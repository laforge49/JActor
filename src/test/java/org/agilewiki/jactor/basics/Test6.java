package org.agilewiki.jactor.basics;

import junit.framework.TestCase;
import org.agilewiki.jactor.JAFuture;
import org.agilewiki.jactor.JAMailboxFactory;
import org.agilewiki.jactor.Mailbox;
import org.agilewiki.jactor.MailboxFactory;

/**
 * Test code.
 */
public class Test6 extends TestCase {
    public void test() throws Exception {
        MailboxFactory mailboxFactory = JAMailboxFactory.newMailboxFactory(10);
        Mailbox mailbox = mailboxFactory.createMailbox();
        Actor6a actor6a = new Actor6a(mailbox);
        JAFuture future = new JAFuture();
        Add add = new Add(5, 11);
        int response = add.send(future, actor6a);
        assertEquals(16, response);
        mailboxFactory.close();
    }
}
