package org.agilewiki.jactor.lpc.calculatorTest;

import org.agilewiki.jactor.Actor;
import org.agilewiki.jactor.ExceptionHandler;
import org.agilewiki.jactor.Mailbox;
import org.agilewiki.jactor.RP;
import org.agilewiki.jactor.factory.ActorFactory;
import org.agilewiki.jactor.lpc.JLPCActor;

/**
 * Test code.
 */
public class PrintingCalculator extends JLPCActor implements _Calculator {
    private Calculator calculator;

    @Override
    public void initialize(Mailbox mailbox, Actor parent, ActorFactory actorFactory)
            throws Exception {
        super.initialize(mailbox, parent, actorFactory);
        calculator = new Calculator();
        calculator.initialize(mailbox, parent);
    }

    public void clear(Clear request, final RP rp)
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

    public void get(Get request, final RP rp)
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

    public void set(final Set request, final RP rp)
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

    public void add(final Add request, final RP rp)
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

    public void subtract(final Subtract request, final RP rp)
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

    public void multiply(final Multiply request, final RP rp)
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

    public void divide(final Divide request, final RP rp)
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
