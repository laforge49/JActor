package org.agilewiki.jactor.advanced.many;

import org.agilewiki.jactor.MailboxFactory;
import org.agilewiki.jactor.RP;
import org.agilewiki.jactor.lpc.JLPCActor;

public class Driver extends JLPCActor {
    public void start(RP rp)
            throws Exception {
        MailboxFactory mailboxFactory = getMailboxFactory();
        ReleaseDriver releaseDriver = new ReleaseDriver();
        releaseDriver.initialize(mailboxFactory.createAsyncMailbox());
        AllocateDriver allocateDriver = new AllocateDriver();
        allocateDriver.initialize(mailboxFactory.createAsyncMailbox());
        rp.processResponse(null);
    }
}
