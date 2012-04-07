package org.agilewiki.jactor.basics;

import org.agilewiki.jactor.ExceptionHandler;
import org.agilewiki.jactor.Mailbox;
import org.agilewiki.jactor.RP;
import org.agilewiki.jactor.lpc.JLPCActor;

public class Actor4 extends JLPCActor {
    public Actor4(Mailbox mailbox) {
        super(mailbox);
    }

    @Override
    protected void processRequest(Object request, final RP rp) throws Exception {
        setExceptionHandler(new ExceptionHandler() {
            @Override
            public void process(Exception exception) throws Exception {
                rp.processResponse(false);
            }
        });
        Greet1.req.send(this, getParent(), new RP<Object>() {
            @Override
            public void processResponse(Object response) throws Exception {
                rp.processResponse(true);
            }
        });
    }
}
