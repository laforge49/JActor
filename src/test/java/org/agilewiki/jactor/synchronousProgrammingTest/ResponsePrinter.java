package org.agilewiki.jactor.synchronousProgrammingTest;

import org.agilewiki.jactor.Actor;
import org.agilewiki.jactor.RP;
import org.agilewiki.jactor.lpc.JLPCActor;
import org.agilewiki.jactor.lpc.SynchronousRequest;

/**
 * Test code.
 */
public class ResponsePrinter extends JLPCActor {
    public void printResponse(SynchronousRequest<?, JLPCActor> request, Actor actor)
            throws Exception {
        System.out.println(request.call((Actor) this, actor));
    }

    @Override
    protected void processRequest(Object request, RP rp) throws Exception {
        if (request.getClass() == PrintResponse.class) {
            PrintResponse printResponse = (PrintResponse) request;
            printResponse(printResponse.getRequest(), printResponse.getActor());
            rp.processResponse(null);
            return;
        }
        throw new UnsupportedOperationException(request.getClass().getName());
    }
}
