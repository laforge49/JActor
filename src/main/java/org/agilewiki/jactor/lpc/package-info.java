/*
 * Copyright 2011 Bill La Forge
 *
 * This file is part of AgileWiki and is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License (LGPL) as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This code is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA
 * or navigate to the following url http://www.gnu.org/licenses/lgpl-2.1.txt
 *
 * Note however that only Scala, Java and JavaScript files are being covered by LGPL.
 * All other files are covered by the Common Public License (CPL).
 * A copy of this license is also included and can be
 * found as well at http://www.opensource.org/licenses/cpl1.0.txt
 */

/**
 * <h2>Introducing JLPCActor--the Basic Actor</h2>
 * <p>
 *     The JLPCActor supports 1-way and 2-way messaging and works with either a mailbox or an asynchronous mailbox.
 *     It also supports exception handlers, JAIterator and SMBuilder. On the other hand, it supports neither message
 *     binding nor actor composition--these are handled by JBActor and JCActor.
 * </p>
 *
 * <h2>A Simple Calculator</h2>
 * <p>
 *     We will implement a simple calculator to show how to build an actor. First, we need some classes that we can
 *     use as requests that can be sent to the calculator.
 * </p>
 * <pre>
 *     public class Clear {}
 *
 *     public class Get {}
 *
 *     public class Set {
 *         public Set(int value) {
 *             this.value = value;
 *         }
 *
 *         public int getValue() {
 *             return value;
 *         }
 *
 *         private int value;
 *     }
 *
 *     public class Add {
 *         public Add(int value) {
 *             this.value = value;
 *         }
 *
 *         public int getValue() {
 *             return value;
 *         }
 *
 *         private int value;
 *     }
 *
 *     public class Subtract {
 *         public Subtract(int value) {
 *             this.value = value;
 *         }
 *
 *         public int getValue() {
 *             return value;
 *         }
 *
 *         private int value;
 *     }
 *
 *     public class Multiply {
 *         public Multiply(int value) {
 *             this.value = value;
 *         }
 *
 *         public int getValue() {
 *             return value;
 *         }
 *
 *         private int value;
 *     }
 *
 *     public class Divide {
 *         public Divide(int value) {
 *             this.value = value;
 *         }
 *
 *         public int getValue() {
 *             return value;
 *         }
 *
 *         private int value;
 *     }
 * </pre>
 * <p>
 *     The Calculator class extends JLPCActor and implements one method, processRequest, which must handle all
 *     incoming request messages. For each type of request, the appropriate operation is performed and the value
 *     of the accumulator is returned.
 * </p>
 * <pre>
 *     public class Calculator extends JLPCActor {
 *         private int accumulator;
 *
 *         public Calculator(Mailbox mailbox) {
 *             super(mailbox);
 *         }
 *
 *         protected void processRequest(Object request, RP rp) throws Exception {
 *             if (request instanceof Clear) clear((Clear) request, rp);
 *             else if (request instanceof Get) get((Get) request, rp);
 *             else if (request instanceof Set) set((Set) request, rp);
 *             else if (request instanceof Add) add((Add) request, rp);
 *             else if (request instanceof Subtract) subtract((Subtract) request, rp);
 *             else if (request instanceof Multiply) multiply((Multiply) request, rp);
 *             else if (request instanceof Divide) divide((Divide) request, rp);
 *             else throw new UnsupportedOperationException(request.getClass().getName());
 *         }
 *
 *         private void clear(Clear request, RP rp) throws Exception {
 *             accumulator = 0;
 *             rp.processResponse(new Integer(accumulator));
 *         }
 *
 *         private void get(Get request, RP rp) throws Exception {
 *             rp.processResponse(new Integer(accumulator));
 *         }
 *
 *         private void set(Set request, RP rp) throws Exception {
 *             accumulator = request.getValue();
 *             rp.processResponse(new Integer(accumulator));
 *         }
 *
 *         private void add(Add request, RP rp) throws Exception {
 *             accumulator = accumulator + request.getValue();
 *             rp.processResponse(new Integer(accumulator));
 *         }
 *
 *         private void subtract(Subtract request, RP rp) throws Exception {
 *             accumulator = accumulator - request.getValue();
 *             rp.processResponse(new Integer(accumulator));
 *         }
 *
 *         private void multiply(Multiply request, RP rp) throws Exception {
 *             accumulator = accumulator * request.getValue();
 *             rp.processResponse(new Integer(accumulator));
 *         }
 *
 *         private void divide(Divide request, RP rp) throws Exception {
 *             accumulator = accumulator / request.getValue();
 *             rp.processResponse(new Integer(accumulator));
 *         }
 *     }
 * </pre>
 * <p>
 *     To test our calculator, we need to create a MailboxFactory, a Mailbox, a Calculator and a JAFuture.
 *     We then use the JAFuture object to set the calculator to 1, to add 2 and then multiply by 3. The result returned from the
 *     add is 9.
 * </p>
 * <pre>
 *     MailboxFactory mailboxFactory = JAMailboxFactory.newMailboxFactory(1);
 *     Mailbox mailbox = mailboxFactory.createMailbox();
 *     Actor calculator = new Calculator(mailbox);
 *     JAFuture future = new JAFuture();
 *     future.send(calculator, new Set(1));
 *     future.send(calculator, new Add(2));
 *     System.err.println(future.send(calculator, new Multiply(3)));
 * </pre>
 * <p>
 *     We should see how we can perform these same operations from within another actor using 2-way messages. It gets a
 *     little messy.
 * </p>
 * <pre>
 *     public class Driver1 extends JLPCActor {
 *         public Driver1(Mailbox mailbox) {
 *             super(mailbox);
 *         }
 *
 *         protected void processRequest(Object request, final RP rp) throws Exception {
 *             final Actor calculator = new Calculator(getMailbox());
 *             send(calculator, new Set(1), new RP() {
 *                 public void processResponse(Object response) throws Exception {
 *                     send(calculator, new Add(2), new RP() {
 *                         public void processResponse(Object response) throws Exception {
 *                             send(calculator, new Multiply(3), rp);
 *                         }
 *                     });
 *                 }
 *             });
 *         }
 *     }
 * </pre>
 * <p>
 *     And here is the test code, which produces the same result as before: 9.
 * </p>
 * <pre>
 *     MailboxFactory mailboxFactory = JAMailboxFactory.newMailboxFactory(1);
 *     Mailbox mailbox = mailboxFactory.createMailbox();
 *     Actor driver = new Driver1(mailbox);
 *     JAFuture future = new JAFuture();
 *     System.err.println(future.send(driver, null));
 * </pre>
 * <p>
 *     Sometimes we can use 1-way messages to simplify our code. Again, this code
 *     produces the same results as above.
 * </p>
 * <pre>
 *     public class Driver2 extends JLPCActor {
 *         public Driver2(Mailbox mailbox) {
 *             super(mailbox);
 *         }
 *
 *         protected void processRequest(Object request, final RP rp)
 *                      throws Exception {
 *             final Actor calculator = new Calculator(getMailbox());
 *             sendEvent(calculator, new Set(1));
 *             sendEvent(calculator, new Add(2));
 *             send(calculator, new Multiply(3), rp);
 *         }
 *     }
 * </pre>
 * <p>
 *     A more general solution is of course to use a state machine.
 * </p>
 * <pre>
 *     public class Driver3 extends JLPCActor {
 *         public Driver3(Mailbox mailbox) {
 *             super(mailbox);
 *         }
 *
 *         protected void processRequest(Object request, final RP rp)
 *                 throws Exception {
 *             final Actor calculator = new Calculator(getMailbox());
 *             final SMBuilder smb = new SMBuilder();
 *             smb._send(calculator, new Set(1));
 *             smb._send(calculator, new Add(2));
 *             smb._send(calculator, new Multiply(3), "result");
 *             smb._return(new ObjectFunc() {
 *                 public Object get(StateMachine sm) {
 *                     return sm.get("result");
 *                 }
 *             });
 *             smb.call(rp);
 *         }
 *     }
 * </pre>
 *
 * <h2>ExceptionHandler</h2>
 * <p>
 *     Here we look at a printing calculator, which uses Calculator to perform the actual
 *     operations. It prints the requests, the results and any raised exceptions.
 * </p>
 * <pre>
 *     public class PrintingCalculator extends JLPCActor {
 *         private Calculator calculator;
 *
 *         public PrintingCalculator(Mailbox mailbox) {
 *             super(mailbox);
 *             calculator = new Calculator(mailbox);
 *         }
 *
 *         protected void processRequest(Object request, RP rp)
 *                 throws Exception {
 *             if (request instanceof Clear) clear((Clear) request, rp);
 *             else if (request instanceof Get) get((Get) request, rp);
 *             else if (request instanceof Set) set((Set) request, rp);
 *             else if (request instanceof Add) add((Add) request, rp);
 *             else if (request instanceof Subtract) subtract((Subtract) request, rp);
 *             else if (request instanceof Multiply) multiply((Multiply) request, rp);
 *             else if (request instanceof Divide) divide((Divide) request, rp);
 *             else throw new UnsupportedOperationException(request.getClass().getName());
 *         }
 *
 *         private void clear(Clear request, final RP rp)
 *                 throws Exception {
 *             setExceptionHandler(new ExceptionHandler() {
 *                 public void process(Exception exception) throws Exception {
 *                     System.out.println("Clear => " + exception);
 *                     rp.processResponse(null);
 *                 }
 *             });
 *             send(calculator, request, new RP() {
 *                 public void processResponse(Object response) throws Exception {
 *                     System.out.println("Clear => " + response);
 *                     rp.processResponse(response);
 *                 }
 *             });
 *         }
 *
 *         private void get(Get request, final RP rp)
 *                 throws Exception {
 *             setExceptionHandler(new ExceptionHandler() {
 *                 public void process(Exception exception) throws Exception {
 *                     System.out.println("Get => " + exception);
 *                     rp.processResponse(null);
 *                 }
 *             });
 *             send(calculator, request, new RP() {
 *                 public void processResponse(Object response) throws Exception {
 *                     System.out.println("Get => " + response);
 *                     rp.processResponse(response);
 *                 }
 *             });
 *         }
 *
 *         private void set(final Set request, final RP rp)
 *                 throws Exception {
 *             setExceptionHandler(new ExceptionHandler() {
 *                 public void process(Exception exception) throws Exception {
 *                     System.out.println("Set " + request.getValue() + " => " + exception);
 *                     rp.processResponse(null);
 *                 }
 *             });
 *             send(calculator, request, new RP() {
 *                 public void processResponse(Object response) throws Exception {
 *                     System.out.println("Set " + request.getValue() + " => " + response);
 *                     rp.processResponse(response);
 *                 }
 *             });
 *         }
 *
 *         private void add(final Add request, final RP rp)
 *                 throws Exception {
 *             setExceptionHandler(new ExceptionHandler() {
 *                 public void process(Exception exception) throws Exception {
 *                     System.out.println("+ " + request.getValue() + " => " + exception);
 *                     rp.processResponse(null);
 *                 }
 *             });
 *             send(calculator, request, new RP() {
 *                 public void processResponse(Object response) throws Exception {
 *                     System.out.println("+ " + request.getValue() + " => " + response);
 *                     rp.processResponse(response);
 *                 }
 *             });
 *         }
 *
 *         private void subtract(final Subtract request, final RP rp)
 *                 throws Exception {
 *             setExceptionHandler(new ExceptionHandler() {
 *                 public void process(Exception exception) throws Exception {
 *                     System.out.println("- " + request.getValue() + " => " + exception);
 *                     rp.processResponse(null);
 *                 }
 *             });
 *             send(calculator, request, new RP() {
 *                 public void processResponse(Object response) throws Exception {
 *                     System.out.println("- " + request.getValue() + " => " + response);
 *                     rp.processResponse(response);
 *                 }
 *             });
 *         }
 *
 *         private void multiply(final Multiply request, final RP rp)
 *                 throws Exception {
 *             setExceptionHandler(new ExceptionHandler() {
 *                 public void process(Exception exception) throws Exception {
 *                     System.out.println("* " + request.getValue() + " => " + exception);
 *                     rp.processResponse(null);
 *                 }
 *             });
 *             send(calculator, request, new RP() {
 *                 public void processResponse(Object response) throws Exception {
 *                     System.out.println("* " + request.getValue() + " => " + response);
 *                     rp.processResponse(response);
 *                 }
 *             });
 *         }
 *
 *         private void divide(final Divide request, final RP rp)
 *                 throws Exception {
 *             setExceptionHandler(new ExceptionHandler() {
 *                 public void process(Exception exception) throws Exception {
 *                     System.out.println("/ " + request.getValue() + " => " + exception);
 *                     rp.processResponse(null);
 *                 }
 *             });
 *             send(calculator, request, new RP() {
 *                 public void processResponse(Object response) throws Exception {
 *                     System.out.println("/ " + request.getValue() + " => " + response);
 *                     rp.processResponse(response);
 *                 }
 *             });
 *         }
 *     }
 * </pre>
 * <p>Test code.</p>
 * <pre>
 *     MailboxFactory mailboxFactory = JAMailboxFactory.newMailboxFactory(1);
 *     Mailbox mailbox = mailboxFactory.createMailbox();
 *     Actor calculator = new PrintingCalculator(mailbox);
 *     JAFuture future = new JAFuture();
 *     future.send(calculator, new Set(1));
 *     future.send(calculator, new Add(2));
 *     future.send(calculator, new Multiply(3));
 *     future.send(calculator, new Divide(0));
 * </pre>
 * <p>Output.</p>
 * <pre>
 *     Set 1 => 1
 *     + 2 => 3
 *     * 3 => 9
 *     / 0 => java.lang.ArithmeticException: / by zero * </pre>
 *
 * <h2>Iterator</h2>
 * <p>
 *     We can use JAIterator and Calculator together to calculate factorials.
 * </p>
 * <p>
 *     First we need a factorial request.
 * </p>
 * <pre>
 *     final public class Factorial {
 *         public Factorial(int value) {
 *             this.value = value;
 *         }
 *
 *         public int getValue() {
 *             return value;
 *         }
 *
 *         private int value;
 *     }
 * </pre>
 * <p>
 *     Now we need an actor to perform the factorial.
 * </p>
 * <pre>
 *     public class FactorialCalculation extends JLPCActor {
 *         public FactorialCalculation(Mailbox mailbox) {
 *             super(mailbox);
 *         }
 *
 *         protected void processRequest(final Object request, final RP rp)
 *                 throws Exception {
 *             final Calculator calculator = new Calculator(getMailbox());
 *             send(calculator, new Set(1), new RP() {
 *                 public void processResponse(Object response) throws Exception {
 *                     JAIterator it = new JAIterator() {
 *                         Factorial factorial = (Factorial) request;
 *                         int max = factorial.getValue();
 *                         int count = 0;
 *                         protected void process(final EP rp1) throws Exception {
 *                             if (count == max) {
 *                                 send(calculator, new Get(), rp1);
 *                                 return;
 *                             }
 *                             count += 1;
 *                             send(calculator, new Multiply(count), new RP() {
 *                                 public void processResponse(Object response) throws Exception {
 *                                     rp1.processResponse(null);
 *                                 }
 *                             });
 *                         }
 *                     };
 *                     it.iterate(rp);
 *                 }
 *             });
 *         }
 *     }
 * </pre>
 * <p>
 *     And then some test code.
 * </p>
 * <pre>
 *     MailboxFactory mailboxFactory = JAMailboxFactory.newMailboxFactory(1);
 *     Mailbox mailbox = mailboxFactory.createMailbox();
 *     Actor calculator = new FactorialCalculation(mailbox);
 *     JAFuture future = new JAFuture();
 *     System.err.println(future.send(calculator, new Factorial(5)));
 * </pre>
 * <p>
 *     The result is 120.
 * </p>
 */

package org.agilewiki.jactor.lpc;
