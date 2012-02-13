package org.agilewiki.jactor.lpc.calculator;

import org.agilewiki.jactor.ExceptionHandler;
import org.agilewiki.jactor.Mailbox;
import org.agilewiki.jactor.RP;
import org.agilewiki.jactor.ResponseProcessor;
import org.agilewiki.jactor.lpc.JLPCActor;

public class PrintingCalculator extends JLPCActor {
    private Calculator calculator;

    public PrintingCalculator(Mailbox mailbox) {
        super(mailbox);
        calculator = new Calculator(mailbox);
    }

    @Override
    protected void processRequest(Object request, RP rp)
            throws Exception {
        if (request instanceof Clear) clear((Clear) request, rp);
        else if (request instanceof Get) get((Get) request, rp);
        else if (request instanceof Set) set((Set) request, rp);
        else if (request instanceof Add) add((Add) request, rp);
        else if (request instanceof Subtract) subtract((Subtract) request, rp);
        else if (request instanceof Multiply) multiply((Multiply) request, rp);
        else if (request instanceof Divide) divide((Divide) request, rp);
        else throw new UnsupportedOperationException(request.getClass().getName());
    }

    private void clear(Clear request, final ResponseProcessor rp)
            throws Exception {
        setExceptionHandler(new ExceptionHandler() {
            @Override
            public void process(Exception exception) throws Exception {
                System.out.println("Clear => " + exception);
                rp.process(null);
            }
        });
        send(calculator, request, new RP() {
            @Override
            public void processResponse(Object response) throws Exception {
                System.out.println("Clear => " + response);
                rp.process(response);
            }
        });
    }

    private void get(Get request, final ResponseProcessor rp)
            throws Exception {
        setExceptionHandler(new ExceptionHandler() {
            @Override
            public void process(Exception exception) throws Exception {
                System.out.println("Get => " + exception);
                rp.process(null);
            }
        });
        send(calculator, request, new RP() {
            @Override
            public void processResponse(Object response) throws Exception {
                System.out.println("Get => " + response);
                rp.process(response);
            }
        });
    }

    private void set(final Set request, final ResponseProcessor rp)
            throws Exception {
        setExceptionHandler(new ExceptionHandler() {
            @Override
            public void process(Exception exception) throws Exception {
                System.out.println("Set " + request.getValue() + " => " + exception);
                rp.process(null);
            }
        });
        send(calculator, request, new RP() {
            @Override
            public void processResponse(Object response) throws Exception {
                System.out.println("Set " + request.getValue() + " => " + response);
                rp.process(response);
            }
        });
    }

    private void add(final Add request, final ResponseProcessor rp)
            throws Exception {
        setExceptionHandler(new ExceptionHandler() {
            @Override
            public void process(Exception exception) throws Exception {
                System.out.println("+ " + request.getValue() + " => " + exception);
                rp.process(null);
            }
        });
        send(calculator, request, new RP() {
            @Override
            public void processResponse(Object response) throws Exception {
                System.out.println("+ " + request.getValue() + " => " + response);
                rp.process(response);
            }
        });
    }

    private void subtract(final Subtract request, final ResponseProcessor rp)
            throws Exception {
        setExceptionHandler(new ExceptionHandler() {
            @Override
            public void process(Exception exception) throws Exception {
                System.out.println("- " + request.getValue() + " => " + exception);
                rp.process(null);
            }
        });
        send(calculator, request, new RP() {
            @Override
            public void processResponse(Object response) throws Exception {
                System.out.println("- " + request.getValue() + " => " + response);
                rp.process(response);
            }
        });
    }

    private void multiply(final Multiply request, final ResponseProcessor rp)
            throws Exception {
        setExceptionHandler(new ExceptionHandler() {
            @Override
            public void process(Exception exception) throws Exception {
                System.out.println("* " + request.getValue() + " => " + exception);
                rp.process(null);
            }
        });
        send(calculator, request, new RP() {
            @Override
            public void processResponse(Object response) throws Exception {
                System.out.println("* " + request.getValue() + " => " + response);
                rp.process(response);
            }
        });
    }

    private void divide(final Divide request, final ResponseProcessor rp)
            throws Exception {
        setExceptionHandler(new ExceptionHandler() {
            @Override
            public void process(Exception exception) throws Exception {
                System.out.println("/ " + request.getValue() + " => " + exception);
                rp.process(null);
            }
        });
        send(calculator, request, new RP() {
            @Override
            public void processResponse(Object response) throws Exception {
                System.out.println("/ " + request.getValue() + " => " + response);
                rp.process(response);
            }
        });
    }
}
