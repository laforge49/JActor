package org.agilewiki.jactor.lpc.calculator;

import org.agilewiki.jactor.ExceptionHandler;
import org.agilewiki.jactor.Mailbox;
import org.agilewiki.jactor.RP;
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

    private void clear(Clear request, final RP rp)
            throws Exception {
        setExceptionHandler(new ExceptionHandler() {
            @Override
            public void process(Exception exception) throws Exception {
                System.out.println("Clear => " + exception);
                rp.processResponse(null);
            }
        });
        send(calculator, request, new RP() {
            @Override
            public void processResponse(Object response) throws Exception {
                System.out.println("Clear => " + response);
                rp.processResponse(response);
            }
        });
    }

    private void get(Get request, final RP rp)
            throws Exception {
        setExceptionHandler(new ExceptionHandler() {
            @Override
            public void process(Exception exception) throws Exception {
                System.out.println("Get => " + exception);
                rp.processResponse(null);
            }
        });
        send(calculator, request, new RP() {
            @Override
            public void processResponse(Object response) throws Exception {
                System.out.println("Get => " + response);
                rp.processResponse(response);
            }
        });
    }

    private void set(final Set request, final RP rp)
            throws Exception {
        setExceptionHandler(new ExceptionHandler() {
            @Override
            public void process(Exception exception) throws Exception {
                System.out.println("Set " + request.getValue() + " => " + exception);
                rp.processResponse(null);
            }
        });
        send(calculator, request, new RP() {
            @Override
            public void processResponse(Object response) throws Exception {
                System.out.println("Set " + request.getValue() + " => " + response);
                rp.processResponse(response);
            }
        });
    }

    private void add(final Add request, final RP rp)
            throws Exception {
        setExceptionHandler(new ExceptionHandler() {
            @Override
            public void process(Exception exception) throws Exception {
                System.out.println("+ " + request.getValue() + " => " + exception);
                rp.processResponse(null);
            }
        });
        send(calculator, request, new RP() {
            @Override
            public void processResponse(Object response) throws Exception {
                System.out.println("+ " + request.getValue() + " => " + response);
                rp.processResponse(response);
            }
        });
    }

    private void subtract(final Subtract request, final RP rp)
            throws Exception {
        setExceptionHandler(new ExceptionHandler() {
            @Override
            public void process(Exception exception) throws Exception {
                System.out.println("- " + request.getValue() + " => " + exception);
                rp.processResponse(null);
            }
        });
        send(calculator, request, new RP() {
            @Override
            public void processResponse(Object response) throws Exception {
                System.out.println("- " + request.getValue() + " => " + response);
                rp.processResponse(response);
            }
        });
    }

    private void multiply(final Multiply request, final RP rp)
            throws Exception {
        setExceptionHandler(new ExceptionHandler() {
            @Override
            public void process(Exception exception) throws Exception {
                System.out.println("* " + request.getValue() + " => " + exception);
                rp.processResponse(null);
            }
        });
        send(calculator, request, new RP() {
            @Override
            public void processResponse(Object response) throws Exception {
                System.out.println("* " + request.getValue() + " => " + response);
                rp.processResponse(response);
            }
        });
    }

    private void divide(final Divide request, final RP rp)
            throws Exception {
        setExceptionHandler(new ExceptionHandler() {
            @Override
            public void process(Exception exception) throws Exception {
                System.out.println("/ " + request.getValue() + " => " + exception);
                rp.processResponse(null);
            }
        });
        send(calculator, request, new RP() {
            @Override
            public void processResponse(Object response) throws Exception {
                System.out.println("/ " + request.getValue() + " => " + response);
                rp.processResponse(response);
            }
        });
    }
}
