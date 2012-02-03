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
import org.agilewiki.jactor.lpc.RequestSource;
import org.agilewiki.jactor.lpc.TransparentException;
import org.agilewiki.jactor.stateMachine.ExtendedResponseProcessor;

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
        final public Actor getParent() {
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
         * Returns true when the parent has the same component.
         *
         * @return True when the parent has the same component.
         */
        @Override
        final public boolean parentHasSameComponent() {
            return parentHasDataItem(getClass().getName());
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
                                       final ResponseProcessor rp,
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
     * The internals of a JBActor.
     */
    final private Internals internals = new Internals() {
        /**
         * Internal concurrent data of the actor.
         */
        final private ConcurrentSkipListMap<String, Object> data = new ConcurrentSkipListMap<String, Object>();

        /**
         * The bindings of the actor.
         */
        final private ConcurrentSkipListMap<String, Binding> bindings =
                new ConcurrentSkipListMap<String, Binding>();

        /**
         * Returns the concurrent data.
         *
         * @return The concurrent data.
         */
        @Override
        final public ConcurrentSkipListMap<String, Object> getData() {
            return data;
        }

        /**
         * Add a binding to the actor.
         *
         * @param requestClassName The class name of the request.
         * @param binding          The binding.
         * @throws IllegalStateException Thrown if there is already a binding for the class.
         */
        @Override
        final public void bind(String requestClassName, Binding binding) throws IllegalStateException {
            if (bindings.containsKey(requestClassName))
                throw new IllegalStateException("Duplicate binding for " + requestClassName);
            bindings.put(requestClassName, binding);
        }

        /**
         * Returns a binding.
         *
         * @param request The request.
         * @return The binding, or null.
         */
        @Override
        final public Binding getBinding(Object request) {
            return bindings.get(request.getClass().getName());
        }

        /**
         * Send a request to a purely synchronous method.
         * An exception will be thrown if the class of the request is not bound to a ConcurrentMethodBinding.
         *
         * @param actor   The target actor.
         * @param request The request.
         * @return The response.
         * @throws Exception Any uncaught exceptions raised while processing the request.
         */
        @Override
        public Object call(Actor actor, ConstrainedRequest request) throws Exception {
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
                               final ResponseProcessor rp)
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
    private Actor parent;

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
            else JBActor.this.processRequest(request, new ResponseProcessor() {
                @Override
                public void process(Object response) {
                    mailbox.response(response);
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
    }

    /**
     * Assign the parent actor.
     * Once assigned, it can not be changed.
     *
     * @param parent The parent actor to which unrecognized requests are forwarded.
     */
    public void setParent(Actor parent) {
        if (this.parent != null)
            throw new UnsupportedOperationException("The parent can not be changed.");
        this.parent = parent;
    }

    /**
     * Returns a binding.
     *
     * @param request The the request.
     * @return The binding, or null.
     */
    final private Binding getBinding(Object request) {
        return internals.getBinding(request);
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
        mailbox.setInitialBufferCapacity(initialBufferCapacity);
    }

    /**
     * Processes a constrained request
     *
     * @param apcRequestSource The originator of the request.
     * @param request          The request.
     * @return The response.
     * @throws Exception Any uncaught exceptions raised while processing the request.
     */
    @Override
    final public Object acceptCall(APCRequestSource apcRequestSource, ConstrainedRequest request) throws Exception {
        RequestSource requestSource = (RequestSource) apcRequestSource;
        Binding binding = getBinding(request);
        if (binding == null) {
            if (parent == null)
                throw new UnsupportedOperationException(request.getClass().getName());
            return parent.acceptCall(requestSource, request);
        }
        if (request instanceof InitializationRequest) {
            if (active)
                throw new UnsupportedOperationException("actor is already active");
            if (!(binding instanceof InitializationMethodBinding))
                throw new UnsupportedOperationException("Request is not bound to a InitializationMethodBinding: " +
                        request.getClass().getName());
            InitializationMethodBinding initializationMethodBinding = (InitializationMethodBinding) binding;
            return initializationMethodBinding.initializationProcessRequest(internals, (InitializationRequest) request);
        } else active = true;
        if (request instanceof ConcurrentRequest) {
            if (!(binding instanceof ConcurrentMethodBinding))
                throw new UnsupportedOperationException("Request is not bound to a ConcurrentMethodBinding: " +
                        request.getClass().getName());
            ConcurrentMethodBinding concurrentMethodBinding = (ConcurrentMethodBinding) binding;
            return concurrentMethodBinding.concurrentProcessRequest(
                    requestReceiver,
                    requestSource,
                    (ConcurrentRequest) request);
        }
        if (request instanceof SynchronousRequest) {
            if (!(binding instanceof SynchronousMethodBinding))
                throw new UnsupportedOperationException("Request is not bound to a SynchronousMethodBinding: " +
                        request.getClass().getName());
            if (requestSource.getMailbox() != getMailbox()) throw new UnsupportedOperationException(
                    "A synchronous request may not be called when the mailboxes are not the same");
            SynchronousMethodBinding synchronousMethodBinding = (SynchronousMethodBinding) binding;
            return synchronousMethodBinding.synchronousProcessRequest(internals, (SynchronousRequest) request);
        }
        throw new IllegalArgumentException(request.getClass().getName());
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
        if (request instanceof InitializationRequest) {
            if (active)
                throw new UnsupportedOperationException("actor is already active");
        } else active = true;
        Binding binding = getBinding(request);
        if (binding != null) {
            binding.acceptRequest(requestReceiver, (RequestSource) apcRequestSource, request, rp);
            return;
        }
        if (parent == null)
            throw new UnsupportedOperationException(request.getClass().getName());
        parent.acceptRequest(apcRequestSource, request, rp);
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
                                      final ResponseProcessor rp,
                                      final Binding binding)
            throws Exception {
        final Mailbox sourceMailbox = requestSource.getMailbox();
        final ExceptionHandler sourceExceptionHandler = requestSource.getExceptionHandler();
        if (sourceMailbox == mailbox) {
            syncProcess(request, rp, sourceExceptionHandler, requestSource, binding);
            return;
        }
        if (mailbox.isAsync() || sourceMailbox == null) {
            asyncSend(requestSource, request, rp, sourceExceptionHandler);
            return;
        }
        final Mailbox srcControllingMailbox = sourceMailbox.getControllingMailbox();
        if (mailbox.getControllingMailbox() == srcControllingMailbox) {
            syncSend(requestSource, request, rp, sourceExceptionHandler, binding);
            return;
        }
        if (!mailbox.acquireControl(srcControllingMailbox)) {
            asyncSend(requestSource, request, rp, sourceExceptionHandler);
            return;
        }
        try {
            syncSend(requestSource, request, rp, sourceExceptionHandler, binding);
        } finally {
            mailbox.sendPendingMessages();
            mailbox.relinquishControl();
            mailbox.dispatchRemaining(srcControllingMailbox);
        }
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
                                 final ResponseProcessor rp,
                                 final ExceptionHandler sourceExceptionHandler) {
        ResponseProcessor rp1 = rp;
        if (!rp.isEvent()) rp1 = new ResponseProcessor() {
            @Override
            public void process(Object response) throws Exception {
                rs.setExceptionHandler(sourceExceptionHandler);
                if (response != null && response instanceof Exception) {
                    asyncException(
                            (Exception) response,
                            sourceExceptionHandler,
                            rs.getMailbox());
                } else try {
                    rp.process(response);
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
     * Process a request when the mailbox is shared.
     *
     * @param request                The request.
     * @param rp                     Processes the response.
     * @param sourceExceptionHandler Exception handler of the source actor.
     * @param requestSource          The source of the request.
     * @param binding                Binds a request class.
     * @throws Exception Any uncaught exceptions raised while processing the request.
     */
    final private void syncProcess(final Object request,
                                   final ResponseProcessor rp,
                                   final ExceptionHandler sourceExceptionHandler,
                                   final RequestSource requestSource,
                                   final Binding binding)
            throws Exception {
        if (rp.isEvent()) {
            try {
                processRequest(request, rp, binding);
            } catch (Exception ex) {
            }
            return;
        }
        try {
            ResponseProcessor rp1 = new ResponseProcessor() {
                @Override
                public void process(Object response) throws Exception {
                    try {
                        rp.process(response);
                    } catch (Exception e) {
                        throw new TransparentException(e);
                    }
                }
            };
            processRequest(request, rp1, binding);
        } catch (TransparentException t) {
            final Exception e = (Exception) t.getCause();
            requestSource.setExceptionHandler(sourceExceptionHandler);
            throw e;
        } catch (Exception e) {
            requestSource.setExceptionHandler(sourceExceptionHandler);
            if (sourceExceptionHandler == null) throw e;
            sourceExceptionHandler.process(e);
        }
        requestSource.setExceptionHandler(sourceExceptionHandler);
    }

    /**
     * Process a request from another mailbox synchronously.
     *
     * @param rs                     The source of the request.
     * @param request                The request.
     * @param rp                     Processes the response.
     * @param binding                Binds a request class.
     * @param sourceExceptionHandler Exception handler of the source actor.
     */
    final private void syncSend(final RequestSource rs,
                                final Object request,
                                final ResponseProcessor rp,
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
            public void process(final Object response)
                    throws Exception {
                requestSource.setExceptionHandler(sourceExceptionHandler);
                if (!async) {
                    sync = true;
                    try {
                        rp.process(response);
                    } catch (Exception e) {
                        throw new TransparentException(e);
                    }
                } else {
                    if (response != null && response instanceof Exception)
                        asyncException((Exception) response, sourceExceptionHandler, rs.getMailbox());
                    else try {
                        Mailbox sourceMailbox = rs.getMailbox();
                        Mailbox srcControllingMailbox = sourceMailbox.getControllingMailbox();
                        Mailbox controllingMailbox = mailbox.getControllingMailbox();
                        if (srcControllingMailbox == controllingMailbox) {
                            rp.process(response);
                        } else if (sourceMailbox.isAsync()) {
                            asyncResponse(rs, request, response, rp);
                        } else if (!mailbox.acquireControl(srcControllingMailbox)) {
                            asyncResponse(rs, request, response, rp);
                        } else {
                            try {
                                rp.process(response);
                            } finally {
                                mailbox.sendPendingMessages();
                                mailbox.relinquishControl();
                                mailbox.dispatchRemaining(srcControllingMailbox);
                            }
                        }
                    } catch (Exception ex) {
                        asyncException(ex, sourceExceptionHandler, rs.getMailbox());
                    }
                }
            }
        };
        try {
            processRequest(request, erp, binding);
            if (!erp.sync) erp.async = true;
        } catch (TransparentException t) {
            final Exception e = (Exception) t.getCause();
            throw e;
        } catch (Exception e) {
            final ExceptionHandler eh = getExceptionHandler();
            if (eh == null) throw e;
            eh.process(e);
        }
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
                                     ResponseProcessor rp) {
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
    final private void processRequest(Object request, ResponseProcessor rp, Binding binding)
            throws Exception {
        if (binding != null) {
            binding.processRequest(internals, request, rp);
            return;
        }
        throw new UnsupportedOperationException(request.getClass().getName());
    }

    /**
     * Returns the concurrent data of the actor.
     *
     * @return The concurrent data of the actor.
     */
    final protected ConcurrentSkipListMap<String, Object> getData() {
        return internals.getData();
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
    final protected boolean parentHasDataItem(String name) {
        if (parent == null) return false;
        return parent.hasDataItem(name);
    }

    /**
     * Add a binding to the actor.
     *
     * @param requestClassName The class name of the request.
     * @param binding          The binding.
     */
    final protected void bind(String requestClassName, Binding binding) {
        internals.bind(requestClassName, binding);
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
                            final ResponseProcessor rp)
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

    /**
     * Returns the actor's parent.
     *
     * @return The actor's parent, or null.
     */
    final public Actor getParent() {
        return parent;
    }
}
