package org.agilewiki.jactor.factory;

import junit.framework.TestCase;
import org.agilewiki.jactor.JAMailboxFactory;
import org.agilewiki.jactor.MailboxFactory;

/**
 * Test code.
 */
public class JFactoryTest extends TestCase {
    public void test() {
        MailboxFactory mailboxFactory = JAMailboxFactory.newMailboxFactory(1);
        try {
            JAFactory f = new JAFactory();
            f.initialize(mailboxFactory.createMailbox());
            f.defineActorType("Foo", Foo.class);
            Foo foo = (Foo) f.newActor("Foo");
            foo.hi();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mailboxFactory.close();
        }
    }
}
