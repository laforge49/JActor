package org.agilewiki.jactor.composite;

import junit.framework.TestCase;
import org.agilewiki.jactor.JAFuture;
import org.agilewiki.jactor.JAMailboxFactory;
import org.agilewiki.jactor.MailboxFactory;

import java.util.ArrayList;

public class ComponentTest extends TestCase {
    public void test() {
        MailboxFactory mailboxFactory = JAMailboxFactory.newMailboxFactory(1);
        try {
            JCActor a = new JCActor(mailboxFactory.createMailbox());
            a.include(C2.class);
            JAFuture future = new JAFuture();
            System.err.println(future.send(a, new Hi()));
            a.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mailboxFactory.close();
        }
    }
}
