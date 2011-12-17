package org.agilewiki.jactor.apc.exceptions;

import org.agilewiki.jactor.apc.ExceptionHandler;
import org.agilewiki.jactor.apc.JAPCActor;
import org.agilewiki.jactor.apc.ResponseDestination;
import org.agilewiki.jactor.concurrent.ThreadManager;

public class Doer extends JAPCActor {
    public Doer(ThreadManager threadManager) {
        super(threadManager);
        System.out.println("Doer constructed "+this);
    }

    @Override
    protected void processRequest(final Object data, final ResponseDestination rd)
            throws Exception {
        setExceptionHandler(new ExceptionHandler() {
            @Override
            public void process(Exception exception) throws Exception {
                System.out.println("Exception caught by Doer");
                exception.printStackTrace();
                rd.process(null);
            }
        });
        System.out.println("Doer "+getExceptionHandler());
        if (data instanceof T1) {
            throw new Exception("Exception thrown in request processing");
        } else {
            rd.process(data);
        }
    }
}
