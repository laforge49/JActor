package org.agilewiki.jactor.multithreadingTest;

import org.agilewiki.jactor.Actor;
import org.agilewiki.jactor.Mailbox;
import org.agilewiki.jactor.RP;
import org.agilewiki.jactor.lpc.JLPCActor;
import org.agilewiki.jactor.lpc.Request;

/**
 * Test code.
 */
public class ResponsePrinter extends JLPCActor {
    public ResponsePrinter(final Mailbox mailbox) {
        super(mailbox);
    }

    @Override
    protected void processRequest(Object request, final RP rp)
            throws Exception {
        Class reqcls = request.getClass();

        if (reqcls == PrintResponse.class) {
            PrintResponse req = (PrintResponse) request;
            Request wrappedRequest = req.getRequest();
            Actor actor = req.getActor();
            wrappedRequest.send(this, actor, new RP() {
                @Override
                public void processResponse(Object response) throws Exception {
                    System.out.println(response);
                    rp.processResponse(null);
                }
            });
            return;
        }

        throw new UnsupportedOperationException(reqcls.getName());
    }
}
