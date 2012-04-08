package org.agilewiki.jactor.basics;

import org.agilewiki.jactor.Mailbox;
import org.agilewiki.jactor.RP;
import org.agilewiki.jactor.lpc.JLPCActor;

public class Actor6a extends JLPCActor {
    private Actor6 actor6;

    public Actor6a(Mailbox mailbox) {
        super(mailbox);
        actor6 = new Actor6(mailbox);
    }

    @Override
    protected void processRequest(Object request, final RP rp) throws Exception {
        if (request.getClass() != Add.class) {
            throw new UnsupportedOperationException(request.getClass().getName());
        }
        Add req = (Add) request;
        SetXY setXY = new SetXY(req.x, req.y);
        setXY.call(this, actor6);
        rp.processResponse(GetSum.req.call(this, actor6));
    }
}
