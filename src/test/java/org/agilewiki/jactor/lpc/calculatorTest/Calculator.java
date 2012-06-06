package org.agilewiki.jactor.lpc.calculatorTest;

import org.agilewiki.jactor.RP;
import org.agilewiki.jactor.lpc.JLPCActor;

/**
 * Test code.
 */
public class Calculator extends JLPCActor implements _Calculator {
    private int accumulator;

    public void clear(Clear request, RP rp) throws Exception {
        accumulator = 0;
        rp.processResponse(new Integer(accumulator));
    }

    public void get(Get request, RP rp) throws Exception {
        rp.processResponse(new Integer(accumulator));
    }

    public void set(Set request, RP rp) throws Exception {
        accumulator = request.getValue();
        rp.processResponse(new Integer(accumulator));
    }

    public void add(Add request, RP rp) throws Exception {
        accumulator = accumulator + request.getValue();
        rp.processResponse(new Integer(accumulator));
    }

    public void subtract(Subtract request, RP rp) throws Exception {
        accumulator = accumulator - request.getValue();
        rp.processResponse(new Integer(accumulator));
    }

    public void multiply(Multiply request, RP rp) throws Exception {
        accumulator = accumulator * request.getValue();
        rp.processResponse(new Integer(accumulator));
    }

    public void divide(Divide request, RP rp) throws Exception {
        accumulator = accumulator / request.getValue();
        rp.processResponse(new Integer(accumulator));
    }
}
