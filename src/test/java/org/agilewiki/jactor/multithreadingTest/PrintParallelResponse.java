package org.agilewiki.jactor.multithreadingTest;

import org.agilewiki.jactor.Actor;
import org.agilewiki.jactor.lpc.Request;

/**
 * Test code.
 */
public class PrintParallelResponse<RESPONSE> extends Request<Object, ParallelResponsePrinter> {
    private int count;
    private PrintResponse<RESPONSE> printResponse;
    private Actor[] responsePrinters;

    public PrintResponse<RESPONSE> getPrintResponse() {
        return printResponse;
    }

    public int getCount() {
        return count;
    }

    public Actor[] getResponsePrinters() {
        return responsePrinters;
    }

    public PrintParallelResponse(int count,
                                 Actor[] responsePrinters,
                                 PrintResponse<RESPONSE> printResponse) {
        this.count = count;
        this.responsePrinters = responsePrinters;

        this.printResponse = printResponse;
    }

    @Override
    public boolean isTargetType(Actor targetActor) {
        return targetActor instanceof ParallelResponsePrinter;
    }
}
