package org.agilewiki.jactor.components.nblock.exceptions;

import junit.framework.TestCase;
import org.agilewiki.jactor.JAFuture;
import org.agilewiki.jactor.JAMailboxFactory;
import org.agilewiki.jactor.MailboxFactory;
import org.agilewiki.jactor.Request;
import org.agilewiki.jactor.bind.Open;
import org.agilewiki.jactor.components.Include;
import org.agilewiki.jactor.components.JCActor;
import org.agilewiki.jactor.components.nbLock.NBLock;

public class NBLockExceptionsTest extends TestCase {
    public void test() {
        MailboxFactory mailboxFactory = JAMailboxFactory.newMailboxFactory(10);
        try {
            JAFuture future = new JAFuture();
            JCActor nblock = new JCActor(mailboxFactory.createAsyncMailbox());
            (new Include(NBLock.class)).call(nblock);
            Open.req.call(nblock);
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

class DoItEx extends Request<Object> {
}
