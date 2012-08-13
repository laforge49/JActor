package org.agilewiki.jactor.multithreadingTest;

import org.agilewiki.jactor.Actor;
import org.agilewiki.jactor.RP;
import org.agilewiki.jactor.lpc.JLPCActor;
import org.agilewiki.jactor.parallel.JAResponseCounter2;

/**
 * Test code.
 */
public class ParallelResponsePrinter extends JLPCActor {
    public void printParallelResponse(int count,
                                      Actor[] responsePrinters,
                                      PrintResponse printResponse,
                                      RP rp) throws Exception {
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
    }
}
