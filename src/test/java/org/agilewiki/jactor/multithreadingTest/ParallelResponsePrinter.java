package org.agilewiki.jactor.multithreadingTest;

import org.agilewiki.jactor.Actor;
import org.agilewiki.jactor.Mailbox;
import org.agilewiki.jactor.RP;
import org.agilewiki.jactor.lpc.JLPCActor;
import org.agilewiki.jactor.parallel.JAResponseCounter2;

/**
 * Test code.
 */
public class ParallelResponsePrinter extends JLPCActor {
    public ParallelResponsePrinter(final Mailbox mailbox) {
        super(mailbox);
    }

    @Override
    protected void processRequest(Object request, RP rp) throws Exception {
        Class reqcls = request.getClass();

        if (reqcls == PrintParallelResponse.class) {
            PrintParallelResponse req = (PrintParallelResponse) request;
            int count = req.getCount();
            Actor[] responsePrinters = req.getResponsePrinters();
            PrintResponse printResponse = req.getPrintResponse();
            JAResponseCounter2 psrp = new JAResponseCounter2(rp);
            int i = 0;
            while (i < count) {
                System.out.println(i);
                printResponse.send(this, responsePrinters[i], psrp);
                i += 1;
            }
            psrp.sent = count;
            psrp.finished();
            System.out.println(count + " sends");
            return;
        }

        throw new UnsupportedOperationException(reqcls.getName());
    }
}
