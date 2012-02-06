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
            JCActor p1 = new JCActor(mailboxFactory.createMailbox());
            (new Include(Properties.class)).call(p1);
            JCActor p2 = new JCActor(p1.getMailbox());
            p2.setParent(p1);
            (new Include(Properties.class)).call(p2);
            (new SetProperty("a", "foo")).call(p1);
            (new SetProperty("b", "bar")).call(p2);
            System.err.println((new GetProperty("a")).call(p2));
            System.err.println((new GetProperty("b")).call(p2));
            System.err.println((new GetProperty("c")).call(p2));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mailboxFactory.close();
        }
    }
}
