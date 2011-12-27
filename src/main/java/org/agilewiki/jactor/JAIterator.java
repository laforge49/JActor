package org.agilewiki.jactor;

/**
 * <p>
 * Iterates over its process method and works within any actor which supports 2-way messages.
 * </p>
 * <p>
 * We can use JAIterator to calculate factorials:
 * </p>
 * <pre>
 *        final int max = 5;
 *        ResponseProcessor printResult = new ResponseProcessor() {
 *            public void process(Object rsp) {
 *                System.out.println(rsp);
 *            }
 *        };
 *
 *        (new JAIterator(printResult) {
 *            int i;
 *            int r;
 *
 *            public void process(ResponseProcessor rp) throws Exception {
 *                if (r == 0) r = 1;
 *                if (i >= max) rp.process(new Integer(r));
 *                else {
 *                    i += 1;
 *                    r = r * i;
 *                    rp.process(null);
 *                }
 *            }
 *        }).iterate();
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
 *     protected void processRequest(Object req, final ResponseProcessor rp)
 *             throws Exception {
 *         final int max = 5;
 *         ResponseProcessor printResult = new ResponseProcessor() {
 *             public void process(Object rsp) throws Exception {
 *                 System.out.println(rsp);
 *                 rp.process(null);
 *             }
 *         };
 *         (new JAIterator(printResult) {
 *             int i;
 *             int r;
 *             Multiplier mp = new Multiplier(getMailbox());
 *
 *             public void process(ResponseProcessor rp) throws Exception {
 *                 if (r == 0) r = 1;
 *                 if (i >= max) rp.process(new Integer(r));
 *                 else {
 *                     i += 1;
 *                     Multiply m = new Multiply();
 *                     m.a = r;
 *                     m.b = i;
 *                     send(mp, m, new ResponseProcessor() {
 *                         public void process(Object rsp) throws Exception {
 *                             r = ((Integer) rsp).intValue();
 *                         }
 *                     });
 *                     rp.process(null);
 *                 }
 *             }
 *         }).iterate();
 *     }
 * }
 * </pre>
 */
abstract public class JAIterator {

    /**
     * Set to true when the response has not been received asynchronously.
     */
    private boolean sync = false;

    /**
     * Set to true when the response has not been received synchronously.
     */
    private boolean async = false;

    /**
     * Processes the final, non-null result.
     */
    private ResponseProcessor responseProcessor;

    /**
     * The internal ResponseProcessor which handles the responses from the call to process.
     */
    private final ResponseProcessor irp = new ResponseProcessor() {
        @Override
        public void process(Object response) throws Exception {
            if (response == null) {
                if (!async) {
                    sync = true;
                } else {
                    iterate(responseProcessor); //not recursive
                }
            } else if (response instanceof JANull) responseProcessor.process(null);
            else responseProcessor.process(response); 
        }
    };

    /**
     * Iterates over the process method.
     *
     * @throws Exception Any uncaught exceptions raised by the process method.
     */
    public void iterate(ResponseProcessor responseProcessor) throws Exception {
        this.responseProcessor = responseProcessor;
        sync = true;
        while (sync) {
            sync = false;
            process(irp);
            if (!sync) {
                async = true;
            }
        }
    }

    /**
     * Perform an iteration.
     *
     * @param responseProcessor Processes the response.
     * @throws Exception Any uncaught exceptions raised by the process method.
     */
    abstract protected void process(ResponseProcessor responseProcessor) throws Exception;
}
