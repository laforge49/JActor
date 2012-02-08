package org.agilewiki.jactor.synchronousProgramming;

import junit.framework.TestCase;
import org.agilewiki.jactor.JAFuture;
import org.agilewiki.jactor.JAMailboxFactory;
import org.agilewiki.jactor.Mailbox;
import org.agilewiki.jactor.components.Include;
import org.agilewiki.jactor.components.JCActor;

public class GreeterTest extends TestCase {
    public void test() {
        JAMailboxFactory mailboxFactory = JAMailboxFactory.newMailboxFactory(1);
        try {
            Mailbox mailbox = mailboxFactory.createMailbox();
            JCActor a = new JCActor(mailbox);
            (new Include(Greeter.class)).call(a);
            JAFuture future = new JAFuture();
            String greeting = (new Hi()).send(future, a);
            System.out.println(greeting);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mailboxFactory.close();
        }
    }
}
