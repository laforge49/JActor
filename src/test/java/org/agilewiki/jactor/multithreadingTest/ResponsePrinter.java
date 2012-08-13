package org.agilewiki.jactor.multithreadingTest;

import org.agilewiki.jactor.Actor;
import org.agilewiki.jactor.RP;
import org.agilewiki.jactor.lpc.JLPCActor;
import org.agilewiki.jactor.lpc.Request;

/**
 * Test code.
 */
public class ResponsePrinter extends JLPCActor {
    public void printResponse(Request wrappedRequest, Actor actor, final RP rp) throws Exception {
        wrappedRequest.send(this, actor, new RP() {
            @Override
            public void processResponse(Object response) throws Exception {
                System.out.println(response);
                rp.processResponse(null);
            }
        });
    }
}
