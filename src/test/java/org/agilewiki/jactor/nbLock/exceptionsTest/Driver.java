package org.agilewiki.jactor.nbLock.exceptionsTest;

import org.agilewiki.jactor.RP;
import org.agilewiki.jactor.lpc.JLPCActor;

/**
 * Test code.
 */
public class Driver
        extends JLPCActor
        implements Does {
    @Override
    public void does(final RP rp) throws Exception {
        DoItEx req = new DoItEx();

        final RP<Object> rpc = new RP<Object>() {
            int count = 3;

            @Override
            public void processResponse(Object response) throws Exception {
                count -= 1;
                if (count == 0)
                    rp.processResponse(null);
            }
        };

        Process p1 = new Process();
        p1.initialize(getMailbox(), this);
        p1.setActorName("1");

        Process p2 = new Process();
        p2.initialize(getMailbox(), this);
        p2.setActorName("2");

        Process p3 = new Process();
        p3.initialize(getMailbox(), this);
        p3.setActorName("3");

        req.send(this, p1, rpc);
        req.send(this, p2, rpc);
        req.send(this, p3, rpc);
    }
}
