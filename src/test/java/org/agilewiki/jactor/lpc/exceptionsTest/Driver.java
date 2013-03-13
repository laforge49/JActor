package org.agilewiki.jactor.lpc.exceptionsTest;

import org.agilewiki.jactor.Actor;
import org.agilewiki.jactor.ExceptionHandler;
import org.agilewiki.jactor.RP;
import org.agilewiki.jactor.lpc.JLPCActor;

/**
 * Test code.
 */
public class Driver extends JLPCActor implements GoReceiver {
    public Actor doer;

    @Override
    public void processRequest(final Go1 request, final RP rp) throws Exception {
        setExceptionHandler(new ExceptionHandler() {
            @Override
            public void process(final Throwable exception) throws Exception {
                System.out.println("Exception caught by Driver");
                rp.processResponse(null);
            }
        });
        send(doer, request, rp);
    }

    @Override
    public void processRequest(final Go2 request, final RP rp) throws Exception {
        setExceptionHandler(new ExceptionHandler() {
            @Override
            public void process(final Throwable exception) throws Exception {
                System.out.println("Exception caught by Driver");
                rp.processResponse(null);
            }
        });
        send(doer, request, new RP() {
            @Override
            public void processResponse(final Object unwrappedResponse)
                    throws Exception {
                throw new Exception("Exception thrown in response processing");
            }
        });
    }
}
