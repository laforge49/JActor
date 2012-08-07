package org.agilewiki.jactor.advanced.allInOne;

import org.agilewiki.jactor.RP;
import org.agilewiki.jactor.lpc.JLPCActor;

import static junit.framework.Assert.assertEquals;

public class AllInOne extends JLPCActor {
    RP pending;

    public void start(final RP rp)
            throws Exception {
        System.out.println("started");
        assertEquals(
                Start.req,
                getMailbox().getCurrentRequest().getUnwrappedRequest());
        System.out.println("sending allocate");
        Allocate.req.send(this, this, new RP<Object>() {
            @Override
            public void processResponse(Object response) throws Exception {
                System.out.println("got allocate response");
                assertEquals(
                        Start.req,
                        getMailbox().getCurrentRequest().getUnwrappedRequest());
                rp.processResponse(null);
            }
        });
        System.out.println("sending release");
        Release.req.send(this, this, new RP<Object>() {
            @Override
            public void processResponse(Object response) throws Exception {
                System.out.println("got release response");
                assertEquals(
                        Start.req,
                        getMailbox().getCurrentRequest().getUnwrappedRequest());
                rp.processResponse(null);
            }
        });
        assertEquals(
                Start.req,
                getMailbox().getCurrentRequest().getUnwrappedRequest());
        System.out.println("finished");
        assertEquals(
                Start.req,
                getMailbox().getCurrentRequest().getUnwrappedRequest());
    }

    public void allocate(RP rp)
            throws Exception {
        System.out.println("got allocate");
        assertEquals(
                Allocate.req,
                getMailbox().getCurrentRequest().getUnwrappedRequest());
        this.pending = rp;
    }

    public void release()
            throws Exception {
        System.out.println("got release");
        assertEquals(
                Release.req,
                getMailbox().getCurrentRequest().getUnwrappedRequest());
        System.out.println("responding to allocate");
        pending.processResponse(null);
    }
}
