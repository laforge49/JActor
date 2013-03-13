package org.agilewiki.jactor.basics;

import org.agilewiki.jactor.ExceptionHandler;
import org.agilewiki.jactor.RP;
import org.agilewiki.jactor.lpc.JLPCActor;

/**
 * Test code.
 */
public class Actor4 extends JLPCActor {
    protected void processRequest(final Validate1 request, final RP rp)
            throws Exception {
        setExceptionHandler(new ExceptionHandler() {
            @Override
            public void process(final Throwable exception) throws Exception {
                rp.processResponse(false);
            }
        });
        Greet1.req.send(this, getParent(), new RP<Object>() {
            @Override
            public void processResponse(final Object response) throws Exception {
                rp.processResponse(true);
            }
        });
    }
}
