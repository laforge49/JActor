package org.agilewiki.jactor.components.properties;

import junit.framework.TestCase;
import org.agilewiki.jactor.JAFuture;
import org.agilewiki.jactor.JAMailboxFactory;
import org.agilewiki.jactor.MailboxFactory;
import org.agilewiki.jactor.components.Include;
import org.agilewiki.jactor.components.JCActor;

public class PropertiesTest extends TestCase {
    public void test() {
        MailboxFactory mailboxFactory = JAMailboxFactory.newMailboxFactory(1);
        try {
            JAFuture future = new JAFuture();
            JCActor p1 = new JCActor(mailboxFactory.createMailbox());
            future.send(p1, new Include(Properties.class));
            JCActor p2 = new JCActor(p1.getMailbox());
            p2.setParent(p1);
            future.send(p2, new Include(Properties.class));
            future.send(p1, new SetProperty("a", "foo"));
            future.send(p2, new SetProperty("b", "bar"));
            System.err.println(future.send(p2, new GetProperty("a")));
            System.err.println(future.send(p2, new GetProperty("b")));
            System.err.println(future.send(p2, new GetProperty("c")));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mailboxFactory.close();
        }
    }
}
