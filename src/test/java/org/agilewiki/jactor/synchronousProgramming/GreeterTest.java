package org.agilewiki.jactor.synchronousProgramming;

import junit.framework.TestCase;
import org.agilewiki.jactor.JAFuture;
import org.agilewiki.jactor.JAMailboxFactory;
import org.agilewiki.jactor.Mailbox;
import org.agilewiki.jactor.MailboxFactory;
import org.agilewiki.jactor.components.*;

public class GreeterTest extends TestCase {
    public void test() {
        MailboxFactory mailboxFactory = JAMailboxFactory.newMailboxFactory(1);
        try {
            Mailbox mailbox = mailboxFactory.createMailbox();
            JCActor a = new JCActor(mailbox);
            (new Include(Greeter.class)).call(a);
            JAFuture future = new JAFuture();
            String greeting = (new Hi()).send(future, a);
            System.err.println(greeting);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mailboxFactory.close();
        }
    }
}
