package org.agilewiki.jactor.components;

import junit.framework.TestCase;
import org.agilewiki.jactor.JAFuture;
import org.agilewiki.jactor.JAMailboxFactory;
import org.agilewiki.jactor.MailboxFactory;
import org.agilewiki.jactor.components.C2;
import org.agilewiki.jactor.components.Hi;
import org.agilewiki.jactor.components.Include;
import org.agilewiki.jactor.components.JCActor;

public class ComponentTest extends TestCase {
    public void test() {
        MailboxFactory mailboxFactory = JAMailboxFactory.newMailboxFactory(1);
        try {
            JAFuture future = new JAFuture();
            JCActor a = new JCActor(mailboxFactory.createMailbox());
            future.send(a, new Include(C2.class));
            System.err.println(future.send(a, new Hi()));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mailboxFactory.close();
        }
    }
}
