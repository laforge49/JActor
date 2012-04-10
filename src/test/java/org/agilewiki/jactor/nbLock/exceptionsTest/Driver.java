package org.agilewiki.jactor.nbLock.exceptionsTest;

import org.agilewiki.jactor.Mailbox;
import org.agilewiki.jactor.RP;
import org.agilewiki.jactor.lpc.JLPCActor;

/**
 * Test code.
 */
public class Driver
        extends JLPCActor
        implements Does {
    public Driver(Mailbox mailbox) {
        super(mailbox);
    }

    @Override
    protected void processRequest(Object request, final RP rp) throws Exception {
        Class reqcls = request.getClass();

        if (reqcls == DoItEx.class) {
            DoItEx req = (DoItEx) request;

            final RP<Object> rpc = new RP<Object>() {
                int count = 3;

                @Override
                public void processResponse(Object response) throws Exception {
                    count -= 1;
                    if (count == 0)
                        rp.processResponse(null);
                }
            };

            Process p1 = new Process(getMailbox());
            p1.setParent(this);
            p1.setActorName("1");

            Process p2 = new Process(getMailbox());
            p2.setParent(this);
            p2.setActorName("2");

            Process p3 = new Process(getMailbox());
            p3.setParent(this);
            p3.setActorName("3");

            req.send(this, p1, rpc);
            req.send(this, p2, rpc);
            req.send(this, p3, rpc);
            return;
        }

        throw new UnsupportedOperationException(reqcls.getName());
    }
}
