package org.agilewiki.jactor.lpc.calculatorTest;

import org.agilewiki.jactor.Mailbox;
import org.agilewiki.jactor.RP;
import org.agilewiki.jactor.lpc.JLPCActor;

/**
 * Test code.
 */
public class Calculator extends JLPCActor {
    private int accumulator;

    public Calculator(Mailbox mailbox) {
        super(mailbox);
    }

    @Override
    protected void processRequest(Object request, RP rp) throws Exception {
        if (request instanceof Clear) clear((Clear) request, rp);
        else if (request instanceof Get) get((Get) request, rp);
        else if (request instanceof Set) set((Set) request, rp);
        else if (request instanceof Add) add((Add) request, rp);
        else if (request instanceof Subtract) subtract((Subtract) request, rp);
        else if (request instanceof Multiply) multiply((Multiply) request, rp);
        else if (request instanceof Divide) divide((Divide) request, rp);
        else throw new UnsupportedOperationException(request.getClass().getName());
    }

    private void clear(Clear request, RP rp) throws Exception {
        accumulator = 0;
        rp.processResponse(new Integer(accumulator));
    }

    private void get(Get request, RP rp) throws Exception {
        rp.processResponse(new Integer(accumulator));
    }

    private void set(Set request, RP rp) throws Exception {
        accumulator = request.getValue();
        rp.processResponse(new Integer(accumulator));
    }

    private void add(Add request, RP rp) throws Exception {
        accumulator = accumulator + request.getValue();
        rp.processResponse(new Integer(accumulator));
    }

    private void subtract(Subtract request, RP rp) throws Exception {
        accumulator = accumulator - request.getValue();
        rp.processResponse(new Integer(accumulator));
    }

    private void multiply(Multiply request, RP rp) throws Exception {
        accumulator = accumulator * request.getValue();
        rp.processResponse(new Integer(accumulator));
    }

    private void divide(Divide request, RP rp) throws Exception {
        accumulator = accumulator / request.getValue();
        rp.processResponse(new Integer(accumulator));
    }
}
