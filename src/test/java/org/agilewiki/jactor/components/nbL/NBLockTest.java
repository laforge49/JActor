package org.agilewiki.jactor.components.nbL;

import junit.framework.TestCase;
import org.agilewiki.jactor.JAFuture;
import org.agilewiki.jactor.JAMailboxFactory;
import org.agilewiki.jactor.MailboxFactory;
import org.agilewiki.jactor.Request;
import org.agilewiki.jactor.bind.Open;
import org.agilewiki.jactor.components.Include;
import org.agilewiki.jactor.components.JCActor;
import org.agilewiki.jactor.components.nbLock.NBLock;

/**
 * Test code.
 */
public class NBLockTest extends TestCase {
    public void test() {
        MailboxFactory mailboxFactory = JAMailboxFactory.newMailboxFactory(10);
        try {
            JAFuture future = new JAFuture();
            JCActor nblock = new JCActor(mailboxFactory.createMailbox());
            (new Include(NBLock.class)).call(nblock);
            Open.req.call(nblock);
            JCActor driver = new JCActor(mailboxFactory.createMailbox());
            driver.setParent(nblock);
            (new Include(Driver.class)).call(driver);
            Open.req.call(driver);
            (new DoIt()).send(future, driver);
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
class DoIt extends Request<Object> {
}
