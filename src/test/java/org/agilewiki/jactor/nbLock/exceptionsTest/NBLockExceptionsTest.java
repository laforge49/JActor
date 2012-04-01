package org.agilewiki.jactor.nbLock.exceptionsTest;

import junit.framework.TestCase;
import org.agilewiki.jactor.Actor;
import org.agilewiki.jactor.JAFuture;
import org.agilewiki.jactor.JAMailboxFactory;
import org.agilewiki.jactor.MailboxFactory;
import org.agilewiki.jactor.lpc.Request;
import org.agilewiki.jactor.nbLock.JANBLock;

/**
 * Test code.
 */
public class NBLockExceptionsTest extends TestCase {
    public void test() {
        MailboxFactory mailboxFactory = JAMailboxFactory.newMailboxFactory(10);
        try {
            JAFuture future = new JAFuture();
            JANBLock nblock = new JANBLock(mailboxFactory.createAsyncMailbox());
            Driver driver = new Driver(mailboxFactory.createAsyncMailbox());
            driver.setParent(nblock);
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
interface Does {
}

/**
 * Test code.
 */
class DoItEx extends Request<Object, Does> {

    /**
     * Returns true when targetActor is an instanceof TARGET_TYPE
     *
     * @param targetActor The actor to be called.
     * @return True when targetActor is an instanceof TARGET_TYPE.
     */
    protected boolean isTargetType(Actor targetActor) {
        return targetActor instanceof Does;
    }
}
