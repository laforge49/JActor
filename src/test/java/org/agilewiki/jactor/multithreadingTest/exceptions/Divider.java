package org.agilewiki.jactor.multithreadingTest.exceptions;

import org.agilewiki.jactor.Actor;
import org.agilewiki.jactor.ExceptionHandler;
import org.agilewiki.jactor.Mailbox;
import org.agilewiki.jactor.RP;
import org.agilewiki.jactor.lpc.JLPCActor;

/**
 * Test code.
 */
public class Divider extends JLPCActor {
    public Divider(Mailbox mailbox) {
        super(mailbox);
    }

    @Override
    protected void processRequest(Object request, final RP rp) throws Exception {
        Class reqcls = request.getClass();

        if (reqcls == SyncDivide.class) {
            SyncDivide req = (SyncDivide) request;
            rp.processResponse(syncDivide(req.getN(), req.getD()));
            return;
        }

        if (reqcls == Divide.class) {
            Divide req = (Divide) request;
            rp.processResponse(req.getN() / req.getD());
            return;
        }

        if (reqcls == ISyncDivide.class) {
            ISyncDivide req = (ISyncDivide) request;
            rp.processResponse(iSyncDivide(req.getN(), req.getD()));
            return;
        }

        if (reqcls == IDivide.class) {
            IDivide req = (IDivide) request;
            setExceptionHandler(new ExceptionHandler() {
                @Override
                public void process(Exception exception) throws Exception {
                    rp.processResponse(null);
                }
            });
            Divide divide = new Divide(req.getN(), req.getD());
            divide.send(this, this, rp);
            return;
        }

        throw new UnsupportedOperationException(reqcls.getName());
    }

    public int syncDivide(int n, int d) {
        return n / d;
    }

    public Integer iSyncDivide(int n, int d)
            throws Exception {
        SyncDivide syncDivide = new SyncDivide(n, d);
        try {
            return syncDivide.call((Actor) this, this);
        } catch (Exception x) {
            return null;
        }
    }
}
