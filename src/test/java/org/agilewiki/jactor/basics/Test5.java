package org.agilewiki.jactor.basics;

import junit.framework.TestCase;
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
        Actor5 actor5 = new Actor5(mailbox);
        actor5.setWidget("foobar");
        String widget = GetWidget.req.call(actor5);
        assertEquals("foobar", widget);
        mailboxFactory.close();
    }
}
