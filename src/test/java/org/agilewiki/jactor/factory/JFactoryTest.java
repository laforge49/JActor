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
            Actor f = new JAFactory(mailboxFactory.createMailbox());
            (new DefineActorType("Foo", Foo.class)).call(f);
            Actor a = (new NewActor("Foo")).call(f);
            (new Hi()).call(a);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mailboxFactory.close();
        }
    }
}
