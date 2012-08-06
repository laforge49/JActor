package org.agilewiki.jactor.advanced.many;

import org.agilewiki.jactor.RP;
import org.agilewiki.jactor.lpc.JLPCActor;

import static junit.framework.Assert.assertEquals;

public class AllocateDriver extends JLPCActor {
    public Doer doer;

    public void startAllocate(final RP rp)
            throws Exception {
        Allocate.req.send(this, doer, new RP<Object>() {
            @Override
            public void processResponse(Object response) throws Exception {
                assertEquals(
                        StartAllocate.req,
                        getMailbox().getCurrentRequest().getUnwrappedRequest());
                rp.processResponse(null);
                /*
                assertEquals(
                        StartAllocate.req,
                        getMailbox().getCurrentRequest().getUnwrappedRequest());
                        */
            }
        });
    }
}
