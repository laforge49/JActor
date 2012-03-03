package org.agilewiki.jactor.multithreading;

import org.agilewiki.jactor.Request;
import org.agilewiki.jactor.components.JCActor;

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
