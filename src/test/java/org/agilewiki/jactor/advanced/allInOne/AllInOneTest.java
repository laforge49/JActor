package org.agilewiki.jactor.advanced.allInOne;

import junit.framework.TestCase;
import org.agilewiki.jactor.JAFuture;
import org.agilewiki.jactor.JAMailboxFactory;
import org.agilewiki.jactor.MailboxFactory;

public class AllInOneTest extends TestCase {
    public void test()
            throws Exception {
        MailboxFactory mailboxFactory = JAMailboxFactory.newMailboxFactory(1);
        try {
            AllInOne aio = new AllInOne();
            aio.initialize(mailboxFactory.createMailbox());
            Start.req.send(new JAFuture(), aio);
        } finally {
            mailboxFactory.close();
        }
    }
}
