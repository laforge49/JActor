package org.agilewiki.jactor.advanced.many;

import org.agilewiki.jactor.MailboxFactory;
import org.agilewiki.jactor.RP;
import org.agilewiki.jactor.advanced.allInOne.*;
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
        System.out.println(count);
        Trial.req.send(Driver.this, Driver.this, new RP<Object>() {
            @Override
            public void processResponse(Object response) throws Exception {
                System.out.println("!!!!!!!!!!!!!!!!!!!!!");
                assertEquals(
                        Start.req,
                        getMailbox().getCurrentRequest().getUnwrappedRequest());
                assertEquals(null, getMailbox().getCurrentRequest().sourceMailbox);
                count += 1;
                if (count < 8) {
                    System.out.println("true");
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
        if ((count & 1) == 0)
            doer.initialize(mailboxFactory.createMailbox());
        else
            doer.initialize(mailboxFactory.createAsyncMailbox());

        ReleaseDriver releaseDriver = new ReleaseDriver();
        releaseDriver.initialize(mailboxFactory.createAsyncMailbox());
        releaseDriver.doer = doer;

        AllocateDriver allocateDriver = new AllocateDriver();
        allocateDriver.initialize(mailboxFactory.createAsyncMailbox());
        allocateDriver.doer = doer;

        System.out.println("driver sending start release");
        Thread.sleep(100);
        StartRelease.req.send(this, releaseDriver, new RP<Object>() {
            @Override
            public void processResponse(Object response) throws Exception {
                System.out.println("start release completed");
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

        getMailbox().sendPendingMessages();

        System.out.println("driver sending start allocate");
        Thread.sleep(10);
        StartAllocate.req.send(this, allocateDriver, new RP<Object>() {
            @Override
            public void processResponse(Object response) throws Exception {
                System.out.println("start allocate completed");
                assertEquals(
                        Trial.req,
                        getMailbox().getCurrentRequest().getUnwrappedRequest());
                assertEquals(getMailbox(), getMailbox().getCurrentRequest().sourceMailbox);
            }
        });
    }
}
