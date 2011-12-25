package org.agilewiki.jactor.lpc.exceptions;

import org.agilewiki.jactor.Actor;
import org.agilewiki.jactor.ExceptionHandler;
import org.agilewiki.jactor.Mailbox;
import org.agilewiki.jactor.ResponseProcessor;
import org.agilewiki.jactor.lpc.JLPCActor;

public class Driver extends JLPCActor {
    private Actor doer;

    public Driver(Mailbox mailbox, Actor doer) {
        super(mailbox);
        this.doer = doer;
    }

    @Override
    protected void processRequest(final Object unwrappedRequest, final ResponseProcessor rd)
            throws Exception {
        setExceptionHandler(new ExceptionHandler() {
            @Override
            public void process(Exception exception) throws Exception {
                System.out.println("Exception caught by Driver");
                rd.process(null);
            }
        });
        if (unwrappedRequest instanceof T1) {
            send(doer, unwrappedRequest, rd);
        } else {
            send(doer, unwrappedRequest, new ResponseProcessor() {
                @Override
                public void process(Object unwrappedResponse) throws Exception {
                    throw new Exception("Exception thrown in response processing");
                }
            });
        }
    }
}
