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
import org.agilewiki.jactor.stateMachine._SMBuilder;

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
     * The internals of a JBActor.
     */
    final protected Internals internals = new Internals();

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
    final private APCRequestProcessor requestProcessor = new APCRequestProcessor() {
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
            JBActor.this.processRequest(request, new ResponseProcessor() {
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
     *
     * @param parent The parent actor to which unrecognized requests are forwarded.
     */
    public void setParent(Actor parent) {
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
    protected Mailbox getMailbox() {
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
        Binding binding = getBinding(request);
        if (binding != null) {
            if (rp == null) internals.acceptRequest((RequestSource) apcRequestSource, request, rp, null);
            else binding.acceptRequest((RequestSource) apcRequestSource, request, rp);
            return;
        }
        if (parent == null)
            throw new UnsupportedOperationException(request.getClass().getName());
        parent.acceptRequest(apcRequestSource, request, rp);
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
        final JARequest jaRequest = new JARequest(
                rs,
                requestProcessor,
                request,
                new ResponseProcessor() {
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
                });
        rs.send(mailbox, jaRequest);
    }

    /**
     * Process a request when the mailbox is shared.
     *
     * @param request                The request.
     * @param rp                     Processes the response.
     * @param sourceExceptionHandler Exception handler of the source actor.
     * @param requestSource          The source of the request.
     * @param methodBinding          Binds a request class to a method.                               
     * @throws Exception Any uncaught exceptions raised while processing the request.
     */
    final private void syncProcess(final Object request,
                                   final ResponseProcessor rp,
                                   final ExceptionHandler sourceExceptionHandler,
                                   final RequestSource requestSource, 
                                   final MethodBinding methodBinding)
            throws Exception {
        try {
            processRequest(request, new ResponseProcessor() {
                @Override
                public void process(Object response) throws Exception {
                    try {
                        rp.process(response);
                    } catch (Exception e) {
                        throw new TransparentException(e);
                    } finally {
                    }
                }
            }, methodBinding);
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
     * @param methodBinding          Binds a request class to a method.                               
     * @param sourceExceptionHandler Exception handler of the source actor.
     */
    final private void syncSend(final RequestSource rs,
                                final Object request,
                                final ResponseProcessor rp,
                                final ExceptionHandler sourceExceptionHandler,
                                final MethodBinding methodBinding)
            throws Exception {
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
            processRequest(request, erp, methodBinding);
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
     * @param binding          Binds a request class.                               
     * @throws Exception Any uncaught exceptions raised while processing the request.
     */
    final private void processRequest(Object request, ResponseProcessor rp, Binding binding)
            throws Exception {
        if (binding != null) {
            binding.processRequest(request, rp);
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
        return internals.data;
    }

    /**
     * Add a binding to the actor.
     *
     * @param requestClass The class name of the request.
     * @param binding      The binding.
     */
    final protected void bind(String requestClass, Binding binding) {
        internals.bind(requestClass, binding);
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
        actor.acceptRequest(requestSource, request, rp);
    }

    /**
     * Returns the mailbox factory.
     *
     * @return The mailbox factory.
     */
    final protected MailboxFactory getMailboxFactory() {
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
        final private ConcurrentSkipListMap<String, Binding> bindings =
                new ConcurrentSkipListMap<String, Binding>();

        /**
         * Add a binding to the actor.
         *
         * @param requestClass The class name of the request.
         * @param binding      The binding.
         */
        final public void bind(String requestClass, Binding binding) {
            binding.internals = this;
            bindings.put(requestClass, binding);
        }

        /**
         * Returns a binding.
         *
         * @param request The request.
         * @return The binding, or null.
         */
        final public Binding getBinding(Object request) {
            return bindings.get(request.getClass().getName());
        }

        /**
         * Wraps and enqueues an unwrapped request in the requester's inbox.
         *
         * @param requestSource The originator of the request.
         * @param request          The request to be sent.
         * @param rp               The request processor.
         * @param methodBinding          Binds a request class to a method.                               
         * @throws Exception Any uncaught exceptions raised while processing the request.
         */
        public void acceptRequest(final RequestSource requestSource,
                                  final Object request,
                                  final ResponseProcessor rp, 
                                  final MethodBinding methodBinding)
                throws Exception {
            final Mailbox sourceMailbox = requestSource.getMailbox();
            final ExceptionHandler sourceExceptionHandler = requestSource.getExceptionHandler();
            if (sourceMailbox == mailbox) {
                syncProcess(request, rp, sourceExceptionHandler, requestSource, methodBinding);
                return;
            }
            if (mailbox.isAsync() || sourceMailbox == null) {
                asyncSend(requestSource, request, rp, sourceExceptionHandler);
                return;
            }
            final Mailbox srcControllingMailbox = sourceMailbox.getControllingMailbox();
            if (mailbox.getControllingMailbox() == srcControllingMailbox) {
                syncSend(requestSource, request, rp, sourceExceptionHandler, methodBinding);
                return;
            }
            if (!mailbox.acquireControl(srcControllingMailbox)) {
                asyncSend(requestSource, request, rp, sourceExceptionHandler);
                return;
            }
            try {
                syncSend(requestSource, request, rp, sourceExceptionHandler, methodBinding);
            } finally {
                mailbox.sendPendingMessages();
                mailbox.relinquishControl();
                mailbox.dispatchRemaining(srcControllingMailbox);
            }
        }

        /**
         * Send a request to another actor.
         *
         * @param actor   The target actor.
         * @param request The request.
         * @param rp      The response processor.
         * @throws Exception Any uncaught exceptions raised while processing the request.
         */
        final public void send(final Actor actor,
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
        final public MailboxFactory getMailboxFactory() {
            return JBActor.this.getMailboxFactory();
        }

        /**
         * Returns the exception handler.
         *
         * @return The exception handler.
         */
        final public ExceptionHandler getExceptionHandler() {
            return JBActor.this.getExceptionHandler();
        }

        /**
         * Assign an exception handler.
         *
         * @param exceptionHandler The exception handler.
         */
        final public void setExceptionHandler(final ExceptionHandler exceptionHandler) {
            JBActor.this.setExceptionHandler(exceptionHandler);
        }
    }

    /**
     * Creates a _SMBuilder.
     */
    public class SMBuilder extends _SMBuilder {
        @Override
        final public void send(Actor actor, Object request, ResponseProcessor rp)
                throws Exception {
            JBActor.this.send(actor, request, rp);
        }
    }
}
