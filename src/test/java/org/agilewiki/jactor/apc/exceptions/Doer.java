package org.agilewiki.jactor.apc.exceptions;

import org.agilewiki.jactor.apc.ExceptionHandler;
import org.agilewiki.jactor.apc.JAPCActor;
import org.agilewiki.jactor.apc.ResponseProcessor;
import org.agilewiki.jactor.concurrent.ThreadManager;

public class Doer extends JAPCActor {
    public Doer(ThreadManager threadManager) {
        super(threadManager);
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
