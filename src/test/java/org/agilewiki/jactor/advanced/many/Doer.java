package org.agilewiki.jactor.advanced.many;

import org.agilewiki.jactor.RP;
import org.agilewiki.jactor.lpc.JLPCActor;

import static junit.framework.Assert.assertEquals;

public class Doer extends JLPCActor {
    RP pending;

    public void release(RP rp)
            throws Exception {
        this.pending = rp;
        System.out.println("doer got release");
        assertEquals(
                Release.req,
                getMailbox().getCurrentRequest().getUnwrappedRequest());
        Thread.sleep(10);
    }

    public void allocate()
            throws Exception {
        System.out.println("doer got allocate");
        assertEquals(
                Allocate.req,
                getMailbox().getCurrentRequest().getUnwrappedRequest());
        Thread.sleep(10);
        pending.processResponse(null);
        assertEquals(
                Allocate.req,
                getMailbox().getCurrentRequest().getUnwrappedRequest());
    }
}
