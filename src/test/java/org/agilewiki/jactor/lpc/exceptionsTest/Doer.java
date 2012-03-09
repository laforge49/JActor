package org.agilewiki.jactor.lpc.exceptionsTest;

import org.agilewiki.jactor.ExceptionHandler;
import org.agilewiki.jactor.Mailbox;
import org.agilewiki.jactor.RP;
import org.agilewiki.jactor.lpc.JLPCActor;

/**
 * Test code.
 */
public class Doer extends JLPCActor {
    public Doer(Mailbox mailbox) {
        super(mailbox);
    }

    @Override
    public void processRequest(final Object request, final RP rp)
            throws Exception {
        setExceptionHandler(new ExceptionHandler() {
            @Override
            public void process(Exception exception) throws Exception {
                System.out.println("Exception caught by Doer");
                rp.processResponse(null);
            }
        });
        if (request instanceof T1) {
            throw new Exception("Exception thrown in request processing");
        } else {
            rp.processResponse(request);
        }
    }
}
