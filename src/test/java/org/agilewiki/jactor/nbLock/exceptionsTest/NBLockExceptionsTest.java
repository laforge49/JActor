package org.agilewiki.jactor.nbLock.exceptionsTest;

import junit.framework.TestCase;
import org.agilewiki.jactor.*;
import org.agilewiki.jactor.lpc.JLPCActor;
import org.agilewiki.jactor.lpc.Request;
import org.agilewiki.jactor.lpc.TargetActor;
import org.agilewiki.jactor.nbLock.JANBLock;

/**
 * Test code.
 */
public class NBLockExceptionsTest extends TestCase {
    public void test() {
        MailboxFactory mailboxFactory = JAMailboxFactory.newMailboxFactory(10);
        try {
            JAFuture future = new JAFuture();
            JANBLock nblock = new JANBLock();
            nblock.initialize(mailboxFactory.createAsyncMailbox());
            Driver driver = new Driver();
            driver.initialize(mailboxFactory.createAsyncMailbox(), nblock);
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
interface Does extends TargetActor {
    public void does(RP rp) throws Exception;
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
    public boolean isTargetType(Actor targetActor) {
        return targetActor instanceof Does;
    }

    @Override
    public void processRequest(JLPCActor targetActor, RP rp) throws Exception {
        ((Does) targetActor).does(rp);
    }
}
