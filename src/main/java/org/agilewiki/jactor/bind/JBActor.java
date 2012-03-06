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
package org.agilewiki.jactor.bind;

import org.agilewiki.jactor.*;
import org.agilewiki.jactor.apc.*;
import org.agilewiki.jactor.bufferedEvents.BufferedEventsDestination;
import org.agilewiki.jactor.bufferedEvents.BufferedEventsQueue;
import org.agilewiki.jactor.events.EventQueue;
import org.agilewiki.jactor.lpc.RequestSource;
import org.agilewiki.jactor.lpc.TransparentException;
import org.agilewiki.jactor.stateMachine.ExtendedResponseProcessor;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * <p>
 * JBActors support concurrent data, request binding and binding inheritance,
 * but the added overhead makes them a bit slower than JLPCActors.
 * However, JBActors are fully interoperable with JLPCActors
 * so you can avoid the overhead when speed is critical.
 * </p>
 * <p>
 * A JBActor can also be assigned a parent actor
 * to which unrecognized requests are forwarded.
 * </p>
 */
public class JBActor implements Actor {
    /**
     * True when the first non-initialization request is received.
     */
    private boolean active;

    /**
     * The API used when a request is received.
     */
    final private RequestReceiver requestReceiver = new RequestReceiver() {
        /**
         * Returns the concurrent data of the actor.
         *
         * @return The concurrent data of the actor.
         */
        @Override
        final public ConcurrentSkipListMap<String, Object> getData() {
            return JBActor.this.getData();
        }

        /**
         * Returns an actor's parent.
         *
         * @return The actor's parent, or null.
         */
        @Override
        final public JBActor getParent() {
            return JBActor.this.getParent();
        }

        /**
         * Returns true when the concurrent data of the parent contains the named data item.
         *
         * @param name  The key for the data item.
         * @return True when the concurrent data of the parent contains the named data item.
         */
        @Override
        final public boolean parentHasDataItem(String name) {
            return JBActor.this.parentHasDataItem(name);
        }

        /**
         * Ensures that the request is processed on the appropriate thread.
         *
         * @param requestSource The originator of the request.
         * @param request       The request to be sent.
         * @param rp            The request processor.
         * @throws Exception Any uncaught exceptions raised while processing the request.
         */
        @Override
        final public void routeRequest(final RequestSource requestSource,
                                       final Object request,
                                       final RP rp,
                                       Binding binding)
                throws Exception {
            JBActor.this.routeRequest(requestSource, request, rp, binding);
        }

        /**
         * Returns the actor's mailbox.
         *
         * @return The actor's mailbox.
         */
        @Override
        final public Mailbox getMailbox() {
            return JBActor.this.getMailbox();
        }

        /**
         * Returns this actor.
         *
         * @return This actor.
         */
        @Override
        final public JBActor getThisActor() {
            return JBActor.this;
        }
    };

    /**
     * The bindings of the actor.
     */
    final private ConcurrentSkipListMap<String, Binding> bindings =
            new ConcurrentSkipListMap<String, Binding>();

    /**
     * Concurrent data of the actor.
     */
    final private ConcurrentSkipListMap<String, Object> data = new ConcurrentSkipListMap<String, Object>();

    /**
     * The internals of a JBActor.
     */
    final private Internals internals = new Internals() {
        /**
         * Send an initialization request.
         * An exception will be thrown if the class of the request is not bound to a ConcurrentMethodBinding.
         *
         * @param actor   The target actor.
         * @param request The request.
         * @return The response.
         * @throws Exception Any uncaught exceptions raised while processing the request.
         */
        @Override
        public Object call(JBActor actor, InitializationRequest request) throws Exception {
            return actor.acceptCall(requestSource, request);
        }

        /**
         * Send a concurrent request.
         * An exception will be thrown if the class of the request is not bound to a ConcurrentMethodBinding.
         *
         * @param actor   The target actor.
         * @param request The request.
         * @return The response.
         * @throws Exception Any uncaught exceptions raised while processing the request.
         */
        @Override
        public Object call(JBActor actor, ConcurrentRequest request) throws Exception {
            return actor.acceptCall(requestSource, request);
        }

        /**
         * Send a synchronous request.
         * An exception will be thrown if the class of the request is not bound to a ConcurrentMethodBinding.
         *
         * @param actor   The target actor.
         * @param request The request.
         * @return The response.
         * @throws Exception Any uncaught exceptions raised while processing the request.
         */
        @Override
        public Object call(JBActor actor, SynchronousRequest request) throws Exception {
            return actor.acceptCall(requestSource, request);
        }

        /**
         * Send a request to another actor.
         *
         * @param actor   The target actor.
         * @param request The request.
         * @param rp      The response processor.
         * @throws Exception Any uncaught exceptions raised while processing the request.
         */
        @Override
        final public void send(final Actor actor,
                               final Object request,
                               final RP rp)
                throws Exception {
            JBActor.this.send(actor, request, rp);

        }

        /**
         * Send a request to another actor and discard any response.
         *
         * @param actor   The target actor.
         * @param request The request.
         */
        @Override
        final public void sendEvent(Actor actor, Object request) {
            JBActor.this.sendEvent(actor, request);
        }

        /**
         * Returns the exception handler.
         *
         * @return The exception handler.
         */
        @Override
        final public ExceptionHandler getExceptionHandler() {
            return JBActor.this.getExceptionHandler();
        }

        /**
         * Assign an exception handler.
         *
         * @param exceptionHandler The exception handler.
         */
        @Override
        final public void setExceptionHandler(final ExceptionHandler exceptionHandler) {
            JBActor.this.setExceptionHandler(exceptionHandler);
        }

        /**
         * Returns this actor.
         *
         * @return This actor.
         */
        @Override
        final public JBActor getThisActor() {
            return JBActor.this;
        }
    };

    /**
     * The parent actor to which unrecognized requests are forwarded.
     */
    private JBActor parent;

    /**
     * The inbox and outbox of the actor.
     */
    private Mailbox mailbox;

    /**
     * Handles callbacks from the mailbox.
     */
    final private RequestProcessor requestProcessor = new RequestProcessor() {
        private ExceptionHandler exceptionHandler;

        @Override
        final public ExceptionHandler getExceptionHandler() {
            return exceptionHandler;
        }

        @Override
        final public void setExceptionHandler(final ExceptionHandler exceptionHandler) {
            this.exceptionHandler = exceptionHandler;
        }

        @Override
        final public void haveEvents() {
            mailbox.dispatchEvents();
        }

        @Override
        final public void processRequest(final JARequest jaRequest) throws Exception {
            Object request = jaRequest.getUnwrappedRequest();
            Binding binding = getBinding(request);
            if (jaRequest.isEvent())
                JBActor.this.processRequest(request, jaRequest.getResponseProcessor(), binding);
            else JBActor.this.processRequest(request, new RP() {
                @Override
                public void processResponse(Object response) {
                    JARequest old = mailbox.getCurrentRequest();
                    mailbox.setCurrentRequest(jaRequest);
                    mailbox.response(response);
                    mailbox.setCurrentRequest(old);
                }
            }, binding);
        }
    };

    /**
     * Serves as the originator of requests sent to other actors.
     */
    final private RequestSource requestSource = new RequestSource() {
        @Override
        final public Mailbox getMailbox() {
            return mailbox;
        }

        @Override
        final public void responseFrom(final BufferedEventsQueue<JAMessage> eventQueue,
                                       final JAResponse japcResponse) {
            eventQueue.send(mailbox, japcResponse);
        }

        @Override
        final public void send(final BufferedEventsDestination<JAMessage> destination,
                               final JARequest japcRequest) {
            mailbox.send(destination, japcRequest);
        }

        @Override
        final public ExceptionHandler getExceptionHandler() {
            return requestProcessor.getExceptionHandler();
        }

        @Override
        final public void setExceptionHandler(final ExceptionHandler exceptionHandler) {
            requestProcessor.setExceptionHandler(exceptionHandler);
        }
    };

    /**
     * Create a JBActor
     *
     * @param mailbox A mailbox which may be shared with other actors.
     */
    public JBActor(final Mailbox mailbox) {
        if (mailbox == null) throw new IllegalArgumentException("mailbox may not be null");
        this.mailbox = mailbox;

        bind(Open.class.getName(), new VoidInitializationMethodBinding<Open>() {
            @Override
            public void initializationProcessRequest(Internals internals, Open request)
                    throws Exception {
                open(internals);
            }
        });
    }

    /**
     * Add a binding to the actor.
     *
     * @param requestClassName The class name of the request.
     * @param binding          The binding.
     * @throws IllegalStateException Thrown if there is already a binding for the class.
     */
    final public void bind(String requestClassName, Binding binding) throws IllegalStateException {
        if (bindings.containsKey(requestClassName))
            throw new IllegalStateException("Duplicate binding for " + requestClassName);
        if (active)
            throw new UnsupportedOperationException("already active");
        bindings.put(requestClassName, binding);
    }

    /**
     * Returns a binding.
     *
     * @param request The request.
     * @return The binding, or null.
     */
    final private Binding getBinding(Object request) {
        return bindings.get(request.getClass().getName());
    }

    /**
     * Assign the parent actor.
     * Once assigned, it can not be changed.
     *
     * @param parent The parent actor to which unrecognized requests are forwarded.
     */
    public void setParent(JBActor parent) {
        if (this.parent != null)
            throw new UnsupportedOperationException("The parent can not be changed.");
        if (active)
            throw new UnsupportedOperationException("already active");
        this.parent = parent;
    }

    /**
     * Returns the actor's parent.
     *
     * @return The actor's parent, or null.
     */
    final public JBActor getParent() {
        return parent;
    }

    /**
     * Returns the actor's mailbox.
     *
     * @return The actor's mailbox.
     */
    public Mailbox getMailbox() {
        return mailbox;
    }

    /**
     * Set the initial capacity for buffered outgoing messages.
     *
     * @param initialBufferCapacity The initial capacity for buffered outgoing messages.
     */
    @Override
    final public void setInitialBufferCapacity(final int initialBufferCapacity) {
        if (active)
            throw new UnsupportedOperationException("already active");
        mailbox.setInitialBufferCapacity(initialBufferCapacity);
    }

    /**
     * Processes an initialization request
     *
     * @param apcRequestSource The originator of the request.
     * @param request          The request.
     * @return The response.
     * @throws Exception Any uncaught exceptions raised while processing the request.
     */
    final public Object acceptCall(APCRequestSource apcRequestSource, InitializationRequest request)
            throws Exception {
        RequestSource requestSource = (RequestSource) apcRequestSource;
        Binding binding = getBinding(request);
        if (binding == null) {
            if (parent == null) {
                throw new UnsupportedOperationException(request.getClass().getName());
            }
            return parent.acceptCall(requestSource, request);
        }
        if (active)
            throw new UnsupportedOperationException("actor is already active");
        if (binding instanceof InitializationMethodBinding) {
            InitializationMethodBinding initializationMethodBinding = (InitializationMethodBinding) binding;
            return initializationMethodBinding.initializationProcessRequest(internals, request);
        }
        if (binding instanceof VoidInitializationMethodBinding) {
            VoidInitializationMethodBinding initializationMethodBinding = (VoidInitializationMethodBinding) binding;
            initializationMethodBinding.initializationProcessRequest(internals, request);
            return null;
        }
        throw new UnsupportedOperationException("InitializationRequest is not bound to a [Void]InitializationMethodBinding: " +
                request.getClass().getName());
    }

    /**
     * Processes a concurrent request
     *
     * @param apcRequestSource The originator of the request.
     * @param request          The request.
     * @return The response.
     * @throws Exception Any uncaught exceptions raised while processing the request.
     */
    final public Object acceptCall(APCRequestSource apcRequestSource, ConcurrentRequest request)
            throws Exception {
        RequestSource requestSource = (RequestSource) apcRequestSource;
        Binding binding = getBinding(request);
        if (binding == null) {
            if (parent == null) {
                throw new UnsupportedOperationException(request.getClass().getName());
            }
            return parent.acceptCall(requestSource, request);
        }
        if (!active) {
            throw new UnsupportedOperationException("actor is not yet active");
        }
        if (binding instanceof ConcurrentMethodBinding) {
            ConcurrentMethodBinding concurrentMethodBinding = (ConcurrentMethodBinding) binding;
            return concurrentMethodBinding.concurrentProcessRequest(requestReceiver, request);
        }
        if (binding instanceof VoidConcurrentMethodBinding) {
            VoidConcurrentMethodBinding concurrentMethodBinding = (VoidConcurrentMethodBinding) binding;
            concurrentMethodBinding.concurrentProcessRequest(requestReceiver, request);
            return null;
        }
        throw new UnsupportedOperationException("ConcurrentRequest is not bound to a [Void]ConcurrentMethodBinding: " +
                request.getClass().getName());
    }

    /**
     * Processes a synchronous request
     *
     * @param apcRequestSource The originator of the request.
     * @param request          The request.
     * @return The response.
     * @throws Exception Any uncaught exceptions raised while processing the request.
     */
    final public Object acceptCall(APCRequestSource apcRequestSource, SynchronousRequest request)
            throws Exception {
        RequestSource requestSource = (RequestSource) apcRequestSource;
        Binding binding = getBinding(request);
        if (binding == null) {
            if (parent == null) {
                throw new UnsupportedOperationException(request.getClass().getName());
            }
            return parent.acceptCall(requestSource, request);
        }
        if (!active) {
            throw new UnsupportedOperationException("actor is not yet active");
        }
        if (requestSource.getMailbox() != getMailbox()) throw new UnsupportedOperationException(
                "A synchronous request may not be called when the mailboxes are not the same");
        if (binding instanceof SynchronousMethodBinding) {
            SynchronousMethodBinding synchronousMethodBinding = (SynchronousMethodBinding) binding;
            return synchronousMethodBinding.synchronousProcessRequest(internals, (SynchronousRequest) request);
        }
        if (binding instanceof VoidSynchronousMethodBinding) {
            VoidSynchronousMethodBinding synchronousMethodBinding = (VoidSynchronousMethodBinding) binding;
            synchronousMethodBinding.synchronousProcessRequest(internals, (SynchronousRequest) request);
            return null;
        }
        throw new UnsupportedOperationException(
                "SynchronousRequest is not bound to a [Void]SynchronousMethodBinding: " +
                        request.getClass().getName());
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
                                    final RP rp)
            throws Exception {
        if (request instanceof InitializationRequest) {
            if (active)
                throw new UnsupportedOperationException("actor is already active");
        } else if (!active) {
            throw new UnsupportedOperationException("actor is not yet active");
        }
        Binding binding = getBinding(request);
        if (binding != null) {
            binding.acceptRequest(requestReceiver, (RequestSource) apcRequestSource, request, rp);
            return;
        }
        if (parent == null) {
            System.err.println(bindings);
            throw new UnsupportedOperationException(request.getClass().getName());
        }
        parent.acceptRequest(apcRequestSource, request, rp);
    }

    /**
     * Marks the actor as active, and able to process only non-initialization requests.
     *
     * @param internals The actor's internals.
     * @throws Exception Any uncaught exceptions raised while processing the open.
     */
    protected void open(Internals internals) throws Exception {
        active = true;
    }

    /**
     * Returns true if the actor has been opened.
     *
     * @return True if the actor has been opened.
     */
    public boolean isOpen() {
        return active;
    }

    /**
     * Ensures that the request is processed on the appropriate thread.
     *
     * @param requestSource The originator of the request.
     * @param request       The request to be sent.
     * @param rp            The request processor.
     * @param binding       Binds a request class.
     * @throws Exception Any uncaught exceptions raised while processing the request.
     */
    final protected void routeRequest(final RequestSource requestSource,
                                      final Object request,
                                      final RP rp,
                                      final Binding binding)
            throws Exception {
        Mailbox sourceMailbox = requestSource.getMailbox();
        ExceptionHandler sourceExceptionHandler = requestSource.getExceptionHandler();
        if (sourceMailbox == mailbox) {
            syncSend(requestSource, request, rp, sourceExceptionHandler, binding);
            return;
        }
        if (sourceMailbox == null) {
            asyncSend(requestSource, request, rp, sourceExceptionHandler);
            return;
        }
        EventQueue<ArrayList<JAMessage>> eventQueue = mailbox.getEventQueue();
        EventQueue<ArrayList<JAMessage>> srcController = sourceMailbox.getEventQueue().getController();
        if (eventQueue.getController() == srcController) {
            syncSend(requestSource, request, rp, sourceExceptionHandler, binding);
            return;
        }
        if (eventQueue.acquireControl(srcController)) {
            try {
                syncSend(requestSource, request, rp, sourceExceptionHandler, binding);
            } finally {
                mailbox.dispatchEvents();
                mailbox.sendPendingMessages();
                eventQueue.relinquishControl();
            }
            return;
        }
        asyncSend(requestSource, request, rp, sourceExceptionHandler);
    }

    /**
     * Process an exception when the response is asynchronous.
     *
     * @param ex            Any exceptions thrown while processing the request or response.
     * @param eh            The exception handler
     * @param sourceMailbox The mailbox of the source actor.
     */
    final private void asyncException(Exception ex, ExceptionHandler eh, Mailbox sourceMailbox) {
        if (eh == null) sourceMailbox.response(ex);
        else try {
            eh.process(ex);
        } catch (Exception ex2) {
            sourceMailbox.response(ex2);
        }
    }

    /**
     * Process a request asynchronously.
     *
     * @param rs                     The source of the request.
     * @param request                The request.
     * @param rp                     Processes the response.
     * @param sourceExceptionHandler Exception handler of the source actor.
     */
    final private void asyncSend(final RequestSource rs,
                                 final Object request,
                                 final RP rp,
                                 final ExceptionHandler sourceExceptionHandler) {
        RP rp1 = rp;
        if (!rp.isEvent()) rp1 = new RP() {
            @Override
            public void processResponse(Object response) throws Exception {
                rs.setExceptionHandler(sourceExceptionHandler);
                if (response != null && response instanceof Exception) {
                    asyncException(
                            (Exception) response,
                            sourceExceptionHandler,
                            rs.getMailbox());
                } else try {
                    rp.processResponse(response);
                } catch (Exception ex) {
                    asyncException(ex, sourceExceptionHandler, rs.getMailbox());
                }
            }
        };
        final JARequest jaRequest = new JARequest(
                rs,
                requestProcessor,
                request,
                rp1);
        rs.send(mailbox, jaRequest);
    }

    /**
     * Process a request from another mailbox synchronously.
     *
     * @param requestSource          The source of the request.
     * @param request                The request.
     * @param rp                     Processes the response.
     * @param binding                Binds a request class.
     * @param sourceExceptionHandler Exception handler of the source actor.
     */
    final private void syncSend(final RequestSource requestSource,
                                final Object request,
                                final RP rp,
                                final ExceptionHandler sourceExceptionHandler,
                                final Binding binding)
            throws Exception {
        if (rp.isEvent()) {
            try {
                processRequest(request, rp, binding);
            } catch (Exception ex) {
            }
            return;
        }
        final ExtendedResponseProcessor erp = new ExtendedResponseProcessor() {
            @Override
            public void processResponse(final Object response)
                    throws Exception {
                requestSource.setExceptionHandler(sourceExceptionHandler);
                if (!async) {
                    sync = true;
                    try {
                        rp.processResponse(response);
                    } catch (Exception e) {
                        throw new TransparentException(e);
                    }
                } else {
                    if (response != null && response instanceof Exception)
                        asyncException((Exception) response, sourceExceptionHandler, requestSource.getMailbox());
                    else try {
                        Mailbox sourceMailbox = requestSource.getMailbox();
                        EventQueue<ArrayList<JAMessage>> sourceEventQueue = sourceMailbox.getEventQueue();
                        EventQueue<ArrayList<JAMessage>> srcController = sourceEventQueue.getController();
                        EventQueue<ArrayList<JAMessage>> eventQueue = mailbox.getEventQueue();
                        EventQueue<ArrayList<JAMessage>> controller = eventQueue.getController();
                        if (srcController == controller) {
                            rp.processResponse(response);
                        } else if (!eventQueue.acquireControl(srcController)) {
                            asyncResponse(requestSource, request, response, rp);
                        } else {
                            try {
                                rp.processResponse(response);
                            } finally {
                                mailbox.dispatchEvents();
                                mailbox.sendPendingMessages();
                                eventQueue.relinquishControl();
                            }
                        }
                    } catch (Exception ex) {
                        asyncException(ex, sourceExceptionHandler, requestSource.getMailbox());
                    }
                }
            }
        };
        try {
            processRequest(request, erp, binding);
            if (!erp.sync) erp.async = true;
        } catch (TransparentException t) {
            requestSource.setExceptionHandler(sourceExceptionHandler);
            throw (Exception) t.getCause();
        } catch (Exception e) {
            requestSource.setExceptionHandler(sourceExceptionHandler);
            ExceptionHandler eh = getExceptionHandler();
            if (eh == null) throw e;
            eh.process(e);
        }
        requestSource.setExceptionHandler(sourceExceptionHandler);
    }

    /**
     * Respond asynchronously to a synchronous request.
     *
     * @param rs       The source of the request.
     * @param request  The request.
     * @param response The response.
     * @param rp       Processes the response.
     */
    final private void asyncResponse(RequestSource rs,
                                     Object request,
                                     Object response,
                                     RP rp) {
        final JARequest jaRequest = new JARequest(
                rs,
                requestProcessor,
                request,
                rp);
        mailbox.setCurrentRequest(jaRequest);
        mailbox.response(response);
    }

    /**
     * The application method for processing requests sent to the actor.
     *
     * @param request A request.
     * @param rp      The response processor.
     * @param binding Binds a request class.
     * @throws Exception Any uncaught exceptions raised while processing the request.
     */
    final private void processRequest(Object request, RP rp, Binding binding)
            throws Exception {
        if (binding != null) {
            setExceptionHandler(null);
            binding.processRequest(internals, request, rp);
            return;
        }
        throw new UnsupportedOperationException(request.getClass().getName());
    }

    /**
     * Returns the concurrent data.
     *
     * @return The concurrent data.
     */
    final public ConcurrentSkipListMap<String, Object> getData() {
        return data;
    }

    /**
     * Returns true when the concurrent data of the actor, or its parent, contains the named data item.
     *
     * @param name The key for the data item.
     * @return True when the concurrent data of the actor, or its parent, contains the named data item.
     */
    final public boolean hasDataItem(String name) {
        if (getData().containsKey(name)) return true;
        return parentHasDataItem(name);
    }

    /**
     * Returns true when the concurrent data of the parent contains the named data item.
     *
     * @param name The key for the data item.
     * @return True when the concurrent data of the parent contains the named data item.
     */
    final public boolean parentHasDataItem(String name) {
        if (parent == null) return false;
        return parent.hasDataItem(name);
    }

    /**
     * Send a request to another actor.
     *
     * @param actor   The target actor.
     * @param request The request.
     * @param rp      The response processor.
     * @throws Exception Any uncaught exceptions raised while processing the request.
     */
    final private void send(final Actor actor,
                            final Object request,
                            final RP rp)
            throws Exception {
        actor.acceptRequest(requestSource, request, rp);
    }

    /**
     * Send a request to another actor and discard any response.
     *
     * @param actor   The target actor.
     * @param request The request.
     */
    final private void sendEvent(Actor actor, Object request) {
        try {
            send(actor, request, JANoResponse.nrp);
        } catch (Exception ex) {
            throw new UnsupportedOperationException("Unexpected exception", ex);
        }
    }

    /**
     * Returns the mailbox factory.
     *
     * @return The mailbox factory.
     */
    final public MailboxFactory getMailboxFactory() {
        return mailbox.getMailboxFactory();
    }

    /**
     * Returns the exception handler.
     *
     * @return The exception handler.
     */
    final protected ExceptionHandler getExceptionHandler() {
        return requestProcessor.getExceptionHandler();
    }

    /**
     * Assign an exception handler.
     *
     * @param exceptionHandler The exception handler.
     */
    final protected void setExceptionHandler(final ExceptionHandler exceptionHandler) {
        requestProcessor.setExceptionHandler(exceptionHandler);
    }
}
