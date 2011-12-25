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
    protected void processRequest(final Object unwrappedRequest, final ResponseProcessor rd)
            throws Exception {
        setExceptionHandler(new ExceptionHandler() {
            @Override
            public void process(Exception exception) throws Exception {
                System.out.println("Exception caught by Doer");
                rd.process(null);
            }
        });
        if (unwrappedRequest instanceof T1) {
            throw new Exception("Exception thrown in request processing");
        } else {
            rd.process(unwrappedRequest);
        }
    }
}
