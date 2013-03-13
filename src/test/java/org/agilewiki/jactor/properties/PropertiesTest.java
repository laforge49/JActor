package org.agilewiki.jactor.properties;

import junit.framework.TestCase;
import org.agilewiki.jactor.JAMailboxFactory;
import org.agilewiki.jactor.MailboxFactory;

/**
 * Test code.
 */
public class PropertiesTest extends TestCase {
    public void test() {
        MailboxFactory mailboxFactory = JAMailboxFactory.newMailboxFactory(1);
        try {
            JAProperties p1 = new JAProperties();
            p1.initialize(mailboxFactory.createMailbox());
            JAProperties p2 = new JAProperties();
            p2.initialize(p1.getMailbox(), p1);
            JAProperties.setProperty(p1, "a", "foo");
            JAProperties.setProperty(p2, "b", "bar");
            String a = (String) JAProperties.getProperty(p2, "a");
            System.out.println(a);
            String b = (String) JAProperties.getProperty(p2, "b");
            System.out.println(b);
            String c = (String) JAProperties.getProperty(p2, "c");
            System.out.println(c);
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {
            mailboxFactory.close();
        }
    }
}
