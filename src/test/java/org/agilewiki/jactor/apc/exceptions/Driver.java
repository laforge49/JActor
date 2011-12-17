package org.agilewiki.jactor.apc.exceptions;

import org.agilewiki.jactor.apc.ExceptionHandler;
import org.agilewiki.jactor.apc.JAPCActor;
import org.agilewiki.jactor.apc.ResponseDestination;
import org.agilewiki.jactor.concurrent.ThreadManager;

public class Driver extends JAPCActor {
    private Doer doer;
    
    public Driver(ThreadManager threadManager) {
        super(threadManager);
        doer = new Doer(threadManager);
    }

    @Override
    protected void processRequest(final Object data, final ResponseDestination rd)
            throws Exception {
        setExceptionHandler(new ExceptionHandler() {
            @Override
            public void process(Exception exception) throws Exception {
                System.out.println("Exception caught by Driver");
                rd.process(null);
            }
        });
        if (data instanceof T1) {
            send(doer, data, rd);
        } else {
            send(doer, data, new ResponseDestination() {
                @Override
                public void process(Object result) throws Exception {
                    throw new Exception("Exception thrown in response processing");
                }
            });
        }
    }
}
