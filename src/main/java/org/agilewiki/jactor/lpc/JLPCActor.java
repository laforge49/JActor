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
package org.agilewiki.jactor.lpc;

import org.agilewiki.jactor.*;
import org.agilewiki.jactor.apc.*;
import org.agilewiki.jactor.bufferedEvents.BufferedEventsDestination;
import org.agilewiki.jactor.bufferedEvents.BufferedEventsQueue;
import org.agilewiki.jactor.stateMachine.ExtendedResponseProcessor;
import org.agilewiki.jactor.stateMachine._SMBuilder;

/**
 * A mostly synchronous implementation of Actor.
 */
abstract public class JLPCActor implements Actor {

    /**
     * The inbox and outbox of the actor.
     */
    private Mailbox mailbox;

    /**
     * Handles callbacks from the mailbox.
     */
    final private APCRequestProcessor requestProcessor = new APCRequestProcessor() {
        private ExceptionHandler exceptionHandler;

        final public ExceptionHandler getExceptionHandler() {
            return exceptionHandler;
        }

        final public void setExceptionHandler(final ExceptionHandler exceptionHandler) {
            this.exceptionHandler = exceptionHandler;
        }

        @Override
        final public void haveEvents() {
            mailbox.dispatchEvents();
        }

        @Override
        final public void processRequest(final JARequest request) throws Exception {
            JLPCActor.this.processRequest(request.getUnwrappedRequest(), new ResponseProcessor() {
                @Override
                public void process(Object unwrappedResponse) {
                    mailbox.response(unwrappedResponse);
                }
            });
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
    };

    /**
     * Create a JLPCActor
     *
     * @param mailbox A mailbox which may be shared with other actors.
     */
    public JLPCActor(final Mailbox mailbox) {
        if (mailbox == null) throw new IllegalArgumentException("mailbox may not be null");
        this.mailbox = mailbox;
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
     * Wraps and enqueues an unwrapped request in the requester's outbox.
     *
     * @param apcRequestSource The originator of the request.
     * @param request          The request to be sent.
     * @param rp               The request processor.
     */
    @Override
    final public void acceptRequest(final APCRequestSource apcRequestSource,
                                    final Object request,
                                    final ResponseProcessor rp)
            throws Exception {
        final RequestSource requestSource = (RequestSource) apcRequestSource;
        final Mailbox sourceMailbox = requestSource.getMailbox();
        if (sourceMailbox == mailbox) {
            syncProcess(request, rp);
            return;
        }
        if (mailbox.isAsync() || sourceMailbox == null) {
            asyncSend(requestSource, request, rp);
            return;
        }
        final Mailbox srcControllingMailbox = sourceMailbox.getControllingMailbox();
        if (mailbox.getControllingMailbox() == srcControllingMailbox) {
            syncSend(requestSource, request, rp);
        } else if (!mailbox.acquireControl(srcControllingMailbox)) {
            asyncSend(requestSource, request, rp);
        } else {
            try {
                syncSend(requestSource, request, rp);
            } finally {
                mailbox.sendPendingMessages();
                mailbox.relinquishControl();
                mailbox.dispatchRemaining(srcControllingMailbox);
            }
        }
    }

    /**
     * Process a request from another mailbox synchronously.
     *
     * @param rs      The source of the request.
     * @param request The request.
     * @param rp      Processes the response.
     */
    final private void syncSend(final RequestSource rs,
                                final Object request,
                                final ResponseProcessor rp)
            throws Exception {
        try {
            processRequest(request, new ResponseProcessor() {
                @Override
                public void process(Object response) throws Exception {
                    Mailbox sourceMailbox = rs.getMailbox();
                    Mailbox srcControllingMailbox = sourceMailbox.getControllingMailbox();
                    Mailbox controllingMailbox = mailbox.getControllingMailbox();
                    if (srcControllingMailbox == controllingMailbox) {
                        syncResponse(response, rp);
                    } else if (sourceMailbox.isAsync()) {
                        asyncResponse(rs, request, response, rp);
                    } else if (!mailbox.acquireControl(srcControllingMailbox)) {
                        asyncResponse(rs, request, response, rp);
                    } else {
                        try {
                            syncResponse(response, rp);
                        } finally {
                            mailbox.sendPendingMessages();
                            mailbox.relinquishControl();
                            mailbox.dispatchRemaining(srcControllingMailbox);
                        }
                    }
                }
            });
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
     * Respond synchronously to a synchronous request.
     *
     * @param response The response.
     * @param rp       Processes the response.
     * @throws Exception Any uncaught exceptions occurring while processing the response.
     */
    final private void syncResponse(Object response, ResponseProcessor rp) throws Exception {
        try {
            rp.process(response);
        } catch (Exception e) {
            throw new TransparentException(e);
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
     * Process a request asynchronously.
     *
     * @param requestSource The source of the request.
     * @param request       The request.
     * @param rp            Processes the response.
     */
    final private void asyncSend(final RequestSource requestSource,
                                 final Object request,
                                 final ResponseProcessor rp) {
        final JARequest jaRequest = new JARequest(
                requestSource,
                requestProcessor,
                request,
                rp);
        requestSource.send(mailbox, jaRequest);
    }

    /**
     * Process a request synchronously.
     *
     * @param request The request.
     * @param rp      Processes the response.
     * @throws Exception Any uncaught exceptions raised while processing the request.
     */
    final private void syncProcess(final Object request,
                                   final ResponseProcessor rp)
            throws Exception {
        try {
            processRequest(request, new ResponseProcessor() {
                @Override
                public void process(Object response) throws Exception {
                    try {
                        rp.process(response);
                    } catch (Exception e) {
                        throw new TransparentException(e);
                    }
                }
            });
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
        final ExceptionHandler exceptionHandler = requestProcessor.getExceptionHandler();
        ExtendedResponseProcessor erp = new ExtendedResponseProcessor() {
            @Override
            public void process(final Object response)
                    throws Exception {
                requestProcessor.setExceptionHandler(exceptionHandler);
                if (!async) {
                    sync = true;
                    if (response != null && response instanceof Exception) throw (Exception) response;
                    rp.process(response);
                } else {
                    if (response != null && response instanceof Exception) asyncException((Exception) response);
                    else try {
                        rp.process(response);
                    } catch (Exception ex) {
                        asyncException(ex);
                    }
                }
            }
        };
        actor.acceptRequest(requestSource, request, erp);
        if (!erp.sync) erp.async = true;
    }

    /**
     * Process an exception when the response is asynchronous.
     *
     * @param ex Any exceptions thrown while processing the request or response.
     */
    final private void asyncException(Exception ex) {
        ExceptionHandler exceptionHandler = getExceptionHandler();
        if (exceptionHandler == null) mailbox.response(ex);
        else try {
            exceptionHandler.process(ex);
        } catch (Exception ex2) {
            mailbox.response(ex2);
        }
    }

    /**
     * Creates a _SMBuilder.
     */
    public class SMBuilder extends _SMBuilder {
        @Override
        final public void send(Actor actor, Object request, ResponseProcessor rp)
                throws Exception {
            JLPCActor.this.send(actor, request, rp);
        }
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
     * The application method for processing requests sent to the actor.
     *
     * @param request           A request.
     * @param responseProcessor The response processor.
     * @throws Exception Any uncaught exceptions raised while processing the request.
     */
    abstract protected void processRequest(Object request, ResponseProcessor responseProcessor)
            throws Exception;
}
