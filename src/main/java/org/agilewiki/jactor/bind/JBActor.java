package org.agilewiki.jactor.bind;

import org.agilewiki.jactor.*;
import org.agilewiki.jactor.apc.APCRequestSource;
import org.agilewiki.jactor.lpc.JLPCActor;
import org.agilewiki.jactor.lpc.RequestSource;

import java.util.concurrent.ConcurrentSkipListMap;

/**
 * JBActors support concurrent data and request binding,
 * but the added overhead makes them a bit slower than JLPCActors.
 * However, JBActors are fully interoperable with JLPCActors
 * so you can avoid the overhead when speed is critical.
 */
public class JBActor extends JLPCActor {
    /**
     * The internals of a JBActor.
     */
    final protected Internals internals = new Internals();

    public Actor parent;

    /**
     * Create a JBActor
     *
     * @param mailbox A mailbox which may be shared with other actors.
     */
    public JBActor(final Mailbox mailbox) {
        super(mailbox);
    }

    /**
     * Add a binding to the actor.
     *
     * @param requestClass The class of the request.
     * @param binding The binding.
     */
    final protected void bind(Class requestClass, Binding binding) {
        internals.bind(requestClass, binding);
    }

    /**
     * Returns a binding.
     *
     * @param requestClass The class of the request.
     * @return The binding, or null.
     */
    final Binding getBinding(Class requestClass) {
        return internals.getBinding(requestClass);
    }

    /**
     * Wraps and enqueues an unwrapped request in the requester's inbox.
     *
     * @param apcRequestSource The originator of the request.
     * @param request          The request to be sent.
     * @param rp               The request processor.
     * @throws Exception Any uncaught exceptions raised while processing the request.
     */
    @Override
    final public void acceptRequest(final APCRequestSource apcRequestSource,
                                      final Object request,
                                      final ResponseProcessor rp)
            throws Exception {
        Binding binding = getBinding(request.getClass());
        if (binding == null) throw new UnsupportedOperationException(request.getClass().getName());
        binding.acceptRequest((RequestSource) apcRequestSource, request, rp);
    }

    /**
     * Wraps and enqueues an unwrapped request in the requester's inbox.
     *
     * @param requestSource The originator of the request.
     * @param request          The request to be sent.
     * @param rp               The request processor.
     * @throws Exception Any uncaught exceptions raised while processing the request.
     */
    final private void _acceptRequest(final RequestSource requestSource,
                                      final Object request,
                                      final ResponseProcessor rp)
            throws Exception {
        super.acceptRequest(requestSource, request, rp);
    }

    /**
     * The application method for processing requests sent to the actor.
     *
     * @param request           A request.
     * @param responseProcessor The response processor.
     * @throws Exception Any uncaught exceptions raised while processing the request.
     */
    @Override
    final protected void processRequest(Object request, ResponseProcessor responseProcessor)
            throws Exception {

    }

    /**
     * JBActor internals.
     */
    final public class Internals {
        /**
         * Internal concurrent data of the actor.
         */
        final public ConcurrentSkipListMap<String, Object> data = new ConcurrentSkipListMap<String, Object>();

        /**
         * The bindings of the actor.
         */
        final private ConcurrentSkipListMap<Class, Binding> bindings =
                new ConcurrentSkipListMap<Class, Binding>();

        /**
         * Add a binding to the actor.
         *
         * @param requestClass The class of the request.
         * @param binding The binding.
         */
        final public void bind(Class requestClass, Binding binding) {
            binding.internals = this;
            bindings.put(requestClass, binding);
        }

        /**
         * Returns a binding.
         *
         * @param requestClass The class of the request.
         * @return The binding, or null.
         */
        final public Binding getBinding(Class requestClass) {
            return bindings.get(requestClass);
        }

        /**
         * Wraps and enqueues an unwrapped request in the requester's inbox.
         *
         * @param requestSource The originator of the request.
         * @param request          The request to be sent.
         * @param rp               The request processor.
         * @throws Exception Any uncaught exceptions raised while processing the request.
         */
        final public void acceptRequest(final RequestSource requestSource,
                                  final Object request,
                                  final ResponseProcessor rp)
                throws Exception {
            _acceptRequest(requestSource, request, rp);
        }

        /**
         * Send a request to another actor.
         *
         * @param actor   The target actor.
         * @param request The request.
         * @param rp      The response processor.
         * @throws Exception Any uncaught exceptions raised while processing the request.
         */
        final protected void send(final Actor actor,
                                  final Object request,
                                  final ResponseProcessor rp)
                throws Exception {
            JBActor.this.send(actor, request, rp);
        }

        /**
         * Returns the mailbox factory.
         *
         * @return The mailbox factory.
         */
        final protected MailboxFactory getMailboxFactory() {
            return JBActor.this.getMailboxFactory();
        }

        /**
         * Returns the exception handler.
         *
         * @return The exception handler.
         */
        final protected ExceptionHandler getExceptionHandler() {
            return JBActor.this.getExceptionHandler();
        }

        /**
         * Assign an exception handler.
         *
         * @param exceptionHandler The exception handler.
         */
        final protected void setExceptionHandler(final ExceptionHandler exceptionHandler) {
            JBActor.this.setExceptionHandler(exceptionHandler);
        }
    }
}
