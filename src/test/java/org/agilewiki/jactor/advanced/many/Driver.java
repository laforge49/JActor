package org.agilewiki.jactor.advanced.many;

import org.agilewiki.jactor.MailboxFactory;
import org.agilewiki.jactor.RP;
import org.agilewiki.jactor.lpc.JLPCActor;

import static junit.framework.Assert.assertEquals;

public class Driver extends JLPCActor {
    int count;
    RP _rp;

    public void start(final RP rp)
            throws Exception {
        assertEquals(
                Start.req,
                getMailbox().getCurrentRequest().getUnwrappedRequest());
        assertEquals(null, getMailbox().getCurrentRequest().sourceMailbox);
        _rp = rp;
        loop();
    }

    void loop()
            throws Exception {
        Trial.req.send(Driver.this, Driver.this, new RP<Object>() {
            @Override
            public void processResponse(Object response) throws Exception {
                assertEquals(
                        Start.req,
                        getMailbox().getCurrentRequest().getUnwrappedRequest());
                assertEquals(null, getMailbox().getCurrentRequest().sourceMailbox);
                count += 1;
                if (count < 4000) {
                    loop();
                    return;
                }
                _rp.processResponse(null);
                assertEquals(
                        Start.req,
                        getMailbox().getCurrentRequest().getUnwrappedRequest());
                assertEquals(null, getMailbox().getCurrentRequest().sourceMailbox);
            }
        });
    }

    public void trial(final RP rp)
            throws Exception {
        MailboxFactory mailboxFactory = getMailboxFactory();

        Doer doer = new Doer();
        if ((count & 1) == 0) {
            doer.initialize(mailboxFactory.createMailbox());
        } else {
            doer.initialize(mailboxFactory.createAsyncMailbox());
        }

        ReleaseDriver releaseDriver = new ReleaseDriver();
        releaseDriver.initialize(mailboxFactory.createMailbox());
        releaseDriver.doer = doer;

        AllocateDriver allocateDriver = new AllocateDriver();
        if ((count & 2) == 0) {
            allocateDriver.initialize(mailboxFactory.createMailbox());
        } else {
            allocateDriver.initialize(mailboxFactory.createAsyncMailbox());
        }
        allocateDriver.doer = doer;

        StartRelease.req.send(this, releaseDriver, new RP<Object>() {
            @Override
            public void processResponse(Object response) throws Exception {
                assertEquals(
                        Trial.req,
                        getMailbox().getCurrentRequest().getUnwrappedRequest());
                assertEquals(getMailbox(), getMailbox().getCurrentRequest().sourceMailbox);
                rp.processResponse(null);
                assertEquals(
                        Trial.req,
                        getMailbox().getCurrentRequest().getUnwrappedRequest());
                assertEquals(getMailbox(), getMailbox().getCurrentRequest().sourceMailbox);
            }
        });

        StartAllocate.req.send(this, allocateDriver, new RP<Object>() {
            @Override
            public void processResponse(Object response) throws Exception {
            }
        });
    }
}
