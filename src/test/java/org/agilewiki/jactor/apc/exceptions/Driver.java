package org.agilewiki.jactor.apc.exceptions;

import org.agilewiki.jactor.ExceptionHandler;
import org.agilewiki.jactor.ResponseProcessor;
import org.agilewiki.jactor.apc.JAPCActor;
import org.agilewiki.jactor.concurrent.ThreadManager;

public class Driver extends JAPCActor {
    private Doer doer;

    public Driver(ThreadManager threadManager) {
        super(threadManager);
        doer = new Doer(threadManager);
    }

    @Override
    protected void processRequest(final Object unwrappedRequest, final ResponseProcessor rd)
            throws Exception {
        setExceptionHandler(new ExceptionHandler() {
            @Override
            public void process(Exception exception) throws Exception {
                System.out.println("Exception caught by Parallel");
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
