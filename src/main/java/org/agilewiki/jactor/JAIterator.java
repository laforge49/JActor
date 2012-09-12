package org.agilewiki.jactor;

import org.agilewiki.jactor.simpleMachine.ExtendedResponseProcessor;

/**
 * <p>
 * Iterates over its process method and works within any actor which supports 2-way messages.
 * </p>
 * <p>
 * We can use JAIterator to calculate factorials:
 * </p>
 * <pre>
 *        final int max = 5;
 *        RP printResult = new RP() {
 *            public void process(Object rsp) {
 *                System.out.println(rsp);
 *            }
 *        };
 *
 *        (new JAIterator() {
 *            int i;
 *            int r = 1;
 *
 *            public void process(RP rp) throws Exception {
 *                if (i >= max) rp.process(new Integer(r));
 *                else {
 *                    i += 1;
 *                    r = r * i;
 *                    rp.process(null);
 *                }
 *            }
 *        }).iterate(printResult);
 * </pre>
 * <p>
 * The JAIterator.process method is called repeatedly until it returns a non-null response,
 * which is then returned by JAIterator. However, the response need not be returned immediately, so the
 * JAIterator.process method can send messages to other actors:
 * </p>
 * <pre>
 * public class Factorial extends JLPCActor {
 *
 *     public Factorial(Mailbox mailbox) {
 *         super(mailbox);
 *     }
 *
 *     protected void processRequest(Object req, final RP rp)
 *             throws Exception {
 *         final int max = 5;
 *         RP printResult = new RP() {
 *             public void process(Object rsp) throws Exception {
 *                 System.out.println(rsp);
 *                 rp.process(null);
 *             }
 *         };
 *         (new JAIterator() {
 *             int i;
 *             int r = 1;
 *             Multiplier mp = new Multiplier(getMailbox());
 *
 *             protected void process(RP rp) throws Exception {
 *                 if (i >= max) rp.process(new Integer(r));
 *                 else {
 *                     i += 1;
 *                     Multiply m = new Multiply();
 *                     m.a = r;
 *                     m.b = i;
 *                     send(mp, m, new RP() {
 *                         public void process(Object rsp) throws Exception {
 *                             r = ((Integer) rsp).intValue();
 *                         }
 *                     });
 *                     rp.process(null);
 *                 }
 *             }
 *         }).iterate(printResult);
 *     }
 * }
 * </pre>
 */
abstract public class JAIterator {
    /**
     * Iterates over the process method.
     *
     * @param responseProcessor The response processor.
     * @throws Exception Any uncaught exceptions raised by the process method.
     */
    public void iterate(final RP responseProcessor) throws Exception {
        ExtendedResponseProcessor erp = new ExtendedResponseProcessor() {
            @Override
            public void processResponse(Object response) throws Exception {
                if (response == null) {
                    if (!async) {
                        sync = true;
                    } else {
                        iterate(responseProcessor); //not recursive
                    }
                } else if (response instanceof JANull) responseProcessor.processResponse(null);
                else responseProcessor.processResponse(response);
            }
        };
        erp.sync = true;
        while (erp.sync) {
            erp.sync = false;
            process(erp);
            if (!erp.sync) {
                erp.async = true;
            }
        }
    }

    /**
     * Perform an iteration.
     *
     * @param responseProcessor Processes the response.
     * @throws Exception Any uncaught exceptions raised by the process method.
     */
    abstract protected void process(RP responseProcessor) throws Exception;
}
