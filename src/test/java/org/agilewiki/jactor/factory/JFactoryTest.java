package org.agilewiki.jactor.factory;

import junit.framework.TestCase;
import org.agilewiki.jactor.Actor;
import org.agilewiki.jactor.JAMailboxFactory;
import org.agilewiki.jactor.MailboxFactory;

/**
 * Test code.
 */
public class JFactoryTest extends TestCase {
    public void test() {
        MailboxFactory mailboxFactory = JAMailboxFactory.newMailboxFactory(1);
        try {
            JAFactory f = new JAFactory(mailboxFactory.createMailbox());
            f.defineActorType("Foo", Foo.class);
            Actor a = f.newActor("Foo");
            (new Hi()).call(a);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mailboxFactory.close();
        }
    }
}
