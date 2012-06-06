package org.agilewiki.jactor.basics;

import junit.framework.TestCase;
import org.agilewiki.jactor.JAMailboxFactory;
import org.agilewiki.jactor.Mailbox;
import org.agilewiki.jactor.MailboxFactory;

/**
 * Test code.
 */
public class Test3 extends TestCase {
    public void test() throws Exception {
        MailboxFactory mailboxFactory = JAMailboxFactory.newMailboxFactory(10);
        Mailbox mailbox = mailboxFactory.createMailbox();
        Actor1 actor1 = new Actor1();
        actor1.initialize(mailbox);
        Actor2 actor2 = new Actor2();
        actor2.initialize(mailbox, actor1);
        Actor3 actor3 = new Actor3();
        actor3.initialize(mailbox, actor2);
        Greet1.req.sendEvent(actor3);
    }
}
