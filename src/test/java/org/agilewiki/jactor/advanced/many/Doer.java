package org.agilewiki.jactor.advanced.many;

import org.agilewiki.jactor.RP;
import org.agilewiki.jactor.lpc.JLPCActor;

import static junit.framework.Assert.assertEquals;

public class Doer extends JLPCActor {
    RP pending;

    public void release(RP rp)
            throws Exception {
        this.pending = rp;
        assertEquals(
                Release.req,
                getMailbox().getCurrentRequest().getUnwrappedRequest());
    }

    public void allocate()
            throws Exception {
        assertEquals(
                Allocate.req,
                getMailbox().getCurrentRequest().getUnwrappedRequest());
        pending.processResponse(null);
    }
}
