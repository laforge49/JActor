package org.agilewiki.jactor.basics;

import org.agilewiki.jactor.Mailbox;
import org.agilewiki.jactor.RP;
import org.agilewiki.jactor.lpc.JLPCActor;

/**
 * Test code.
 */
public class Actor6 extends JLPCActor {
    private int x;
    private int y;

    public Actor6(Mailbox mailbox) {
        super(mailbox);
    }

    Integer getSum() {
        return x + y;
    }

    void setXY(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    protected void processRequest(Object request, final RP rp) throws Exception {
        Class reqcls = request.getClass();

        if (reqcls == GetSum.class) {
            rp.processResponse(getSum());
            return;
        }

        if (reqcls == SetXY.class) {
            SetXY req = (SetXY) request;
            setXY(req.x, req.y);
            rp.processResponse(null);
            return;
        }

        throw new UnsupportedOperationException(reqcls.getName());
    }
}
