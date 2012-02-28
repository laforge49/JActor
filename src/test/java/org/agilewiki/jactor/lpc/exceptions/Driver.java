package org.agilewiki.jactor.lpc.exceptions;

import org.agilewiki.jactor.Actor;
import org.agilewiki.jactor.ExceptionHandler;
import org.agilewiki.jactor.Mailbox;
import org.agilewiki.jactor.RP;
import org.agilewiki.jactor.lpc.JLPCActor;

public class Driver extends JLPCActor {
    private Actor doer;

    public Driver(Mailbox mailbox, Actor doer) {
        super(mailbox);
        this.doer = doer;
    }

    @Override
    public void processRequest(final Object unwrappedRequest, final RP rd)
            throws Exception {
        setExceptionHandler(new ExceptionHandler() {
            @Override
            public void process(Exception exception) throws Exception {
                System.out.println("Exception caught by JAParallel");
                rd.processResponse(null);
            }
        });
        if (unwrappedRequest instanceof T1) {
            send(doer, unwrappedRequest, rd);
        } else {
            send(doer, unwrappedRequest, new RP() {
                @Override
                public void processResponse(Object unwrappedResponse) throws Exception {
                    throw new Exception("Exception thrown in response processing");
                }
            });
        }
    }
}
