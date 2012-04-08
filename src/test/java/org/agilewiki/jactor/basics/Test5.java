package org.agilewiki.jactor.basics;

import junit.framework.TestCase;
import org.agilewiki.jactor.JAMailboxFactory;
import org.agilewiki.jactor.Mailbox;
import org.agilewiki.jactor.MailboxFactory;

public class Test5 extends TestCase {
    public void test() throws Exception {
        MailboxFactory mailboxFactory = JAMailboxFactory.newMailboxFactory(10);
        Mailbox mailbox = mailboxFactory.createMailbox();
        Actor5 actor5 = new Actor5(mailbox);
        SetWidget setWidget = new SetWidget("foobar");
        setWidget.call(actor5);
        String widget = GetWidget.req.call(actor5);
        assertEquals("foobar", widget);
        Exception ex = null;
        try {
            setWidget.call(actor5);
        } catch (Exception _ex) {
            ex = _ex;
        }
        assertNotNull(ex);
        mailboxFactory.close();
    }
}
