package org.agilewiki.jactor.lpc.exceptions;

import org.agilewiki.jactor.ExceptionHandler;
import org.agilewiki.jactor.Mailbox;
import org.agilewiki.jactor.ResponseProcessor;
import org.agilewiki.jactor.lpc.JLPCActor;

public class Doer extends JLPCActor {
    public Doer(Mailbox mailbox) {
        super(mailbox);
    }

    @Override
    protected void processRequest(final Object request, final ResponseProcessor rp)
            throws Exception {
        setExceptionHandler(new ExceptionHandler() {
            @Override
            public void process(Exception exception) throws Exception {
                System.out.println("Exception caught by Doer");
                rp.process(null);
            }
        });
        if (request instanceof T1) {
            throw new Exception("Exception thrown in request processing");
        } else {
            rp.process(request);
        }
    }
}
