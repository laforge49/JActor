package org.agilewiki.jactor.multithreading;

import org.agilewiki.jactor.bind.Request;
import org.agilewiki.jactor.components.JCActor;

public class PrintParallelResponse<RESPONSE_TYPE> extends Request {
    private int count;
    private PrintResponse<RESPONSE_TYPE> printResponse;
    private JCActor[] responsePrinters;

    public PrintResponse<RESPONSE_TYPE> getPrintResponse() {
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
                                 PrintResponse<RESPONSE_TYPE> printResponse) {
        this.count = count;
        this.responsePrinters = responsePrinters;

        this.printResponse = printResponse;
    }
}
