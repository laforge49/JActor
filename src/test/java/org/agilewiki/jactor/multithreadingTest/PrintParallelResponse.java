package org.agilewiki.jactor.multithreadingTest;

import org.agilewiki.jactor.components.JCActor;
import org.agilewiki.jactor.lpc.Request;

/**
 * Test code.
 */
public class PrintParallelResponse<RESPONSE> extends Request<Object> {
    private int count;
    private PrintResponse<RESPONSE> printResponse;
    private JCActor[] responsePrinters;

    public PrintResponse<RESPONSE> getPrintResponse() {
        return printResponse;
    }

    public int getCount() {
        return count;
    }

    public JCActor[] getResponsePrinters() {
        return responsePrinters;
    }

    public PrintParallelResponse(int count,
                                 JCActor[] responsePrinters,
                                 PrintResponse<RESPONSE> printResponse) {
        this.count = count;
        this.responsePrinters = responsePrinters;

        this.printResponse = printResponse;
    }
}
