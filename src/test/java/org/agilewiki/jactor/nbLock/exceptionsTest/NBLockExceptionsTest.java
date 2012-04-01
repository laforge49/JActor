package org.agilewiki.jactor.nbLock.exceptionsTest;

import junit.framework.TestCase;
import org.agilewiki.jactor.Actor;
import org.agilewiki.jactor.JAFuture;
import org.agilewiki.jactor.JAMailboxFactory;
import org.agilewiki.jactor.MailboxFactory;
import org.agilewiki.jactor.bind.AsyncRequest;
import org.agilewiki.jactor.bind.Open;
import org.agilewiki.jactor.components.Include;
import org.agilewiki.jactor.components.JCActor;
import org.agilewiki.jactor.nbLock.JANBLock;

/**
 * Test code.
 */
public class NBLockExceptionsTest extends TestCase {
    public void test() {
        MailboxFactory mailboxFactory = JAMailboxFactory.newMailboxFactory(10);
        try {
            JAFuture future = new JAFuture();
            Actor nblock = new JANBLock(mailboxFactory.createAsyncMailbox());
            JCActor driver = new JCActor(mailboxFactory.createAsyncMailbox());
            driver.setParent(nblock);
            (new Include(Driver.class)).call(driver);
            Open.req.call(driver);
            (new DoItEx()).send(future, driver);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mailboxFactory.close();
        }
    }
}

/**
 * Test code.
 */
class DoItEx extends AsyncRequest<Object> {
}
