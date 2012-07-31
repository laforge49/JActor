package org.agilewiki.jactor.advanced.many;

import junit.framework.TestCase;
import org.agilewiki.jactor.JAFuture;
import org.agilewiki.jactor.JAMailboxFactory;
import org.agilewiki.jactor.MailboxFactory;

public class ManyTest extends TestCase {
    public void test()
            throws Exception {
        MailboxFactory mailboxFactory = JAMailboxFactory.newMailboxFactory(100);
        try {
            Driver driver = new Driver();
            driver.initialize(mailboxFactory.createAsyncMailbox());
            Start.req.send(new JAFuture(), driver);
        } finally {
            mailboxFactory.close();
        }
    }
}
