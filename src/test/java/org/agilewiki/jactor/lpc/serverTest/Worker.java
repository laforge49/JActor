package org.agilewiki.jactor.lpc.serverTest;

import org.agilewiki.jactor.Mailbox;
import org.agilewiki.jactor.RP;
import org.agilewiki.jactor.lpc.JLPCActor;

import java.util.Random;

/**
 * Test code.
 */
public class Worker extends JLPCActor {
    public Worker(Mailbox mailbox) {
        super(mailbox);
    }

    @Override
    protected void processRequest(Object request, RP rp) throws Exception {
        WorkRequest wr = (WorkRequest) request;
        System.out.println("start request " + wr.id);
        //sleep some times,simulate db read write
        int delay = 10 * new Random().nextInt(10);
        Thread.sleep(delay);
        System.out.println("Performed request " + wr.id + " delay " + delay);
        rp.processResponse(null); //all done
    }
}
