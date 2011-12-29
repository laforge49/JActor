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
import org.agilewiki.jactor.stateMachine.State;
import org.agilewiki.jactor.stateMachine._StateMachine;

/**
 * A mostly synchronous implementation of Actor.
 */
abstract public class JLPCActor implements Actor {

    /**
     * The inbox and outbox of the actor.
     */
    private Mailbox mailbox;

    /**
     * The state of the currently active state machine.
     */
    private State state;

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
     * @param unwrappedRequest The unwrapped request to be sent.
     * @param rd               The request processor.
     */
    @Override
    final public void acceptRequest(final APCRequestSource apcRequestSource,
                                    final Object unwrappedRequest,
                                    final ResponseProcessor rd)
            throws Exception {
        final RequestSource requestSource = (RequestSource) apcRequestSource;
        final Mailbox sourceMailbox = requestSource.getMailbox();
        if (sourceMailbox == mailbox) {
            syncProcess(unwrappedRequest, rd);
            return;
        }
        if (mailbox.isAsync() || sourceMailbox == null) {
            asyncSend(apcRequestSource, unwrappedRequest, rd);
            return;
        }
        final Mailbox srcControllingMailbox = sourceMailbox.getControllingMailbox();
        if (mailbox.getControllingMailbox() == srcControllingMailbox) {
            syncProcess(unwrappedRequest, rd);
        } else if (!mailbox.acquireControl(srcControllingMailbox)) {
            asyncSend(apcRequestSource, unwrappedRequest, rd);
            return;
        } else {
            try {
                syncProcess(unwrappedRequest, rd);
            } finally {
                mailbox.relinquishControl();
                mailbox.dispatchRemaining(srcControllingMailbox);
            }
        }
    }

    /**
     * Process a request asynchronously.
     *
     * @param apcRequestSource The source of the request.
     * @param unwrappedRequest The request.
     * @param rd               Processes the response.
     */
    final private void asyncSend(final APCRequestSource apcRequestSource,
                                 final Object unwrappedRequest,
                                 final ResponseProcessor rd) {
        final JARequest japcRequest = new JARequest(apcRequestSource,
                requestProcessor, unwrappedRequest, rd);
        apcRequestSource.send(mailbox, japcRequest);
    }

    /**
     * Process a request synchronously.
     *
     * @param unwrappedRequest The request.
     * @param rd               Processes the response.
     * @throws Exception Any uncaught exceptions raised while processing the request.
     */
    final private void syncProcess(final Object unwrappedRequest,
                                   final ResponseProcessor rd)
            throws Exception {
        try {
            processRequest(unwrappedRequest, new ResponseProcessor() {
                @Override
                public void process(Object unwrappedResponse) throws Exception {
                    try {
                        rd.process(unwrappedResponse);
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
     * Send an unwrapped request to another actor.
     *
     * @param actor            The target actor.
     * @param unwrappedRequest The unwrapped request.
     * @param rd1              The response processor.
     * @throws Exception Any uncaught exceptions raised while processing the request.
     */
    final protected void send(final Actor actor,
                              final Object unwrappedRequest,
                              final ResponseProcessor rd1)
            throws Exception {
        ResponseProcessor rd2 = rd1;
        final ExceptionHandler exceptionHandler = requestProcessor.getExceptionHandler();
        if (exceptionHandler != null) rd2 = new ResponseProcessor() {
            @Override
            public void process(final Object unwrappedResponse)
                    throws Exception {
                requestProcessor.setExceptionHandler(exceptionHandler);
                rd1.process(unwrappedResponse);
            }
        };
        actor.acceptRequest(requestSource, unwrappedRequest, rd2);
    }

    /**
     * Creates a _StateMachine.
     *
     * @return The new _StateMachine.
     */
    public class SMBuilder extends _StateMachine {
        @Override
        final protected State getState() {
            return state;
        }

        @Override
        final protected void setState(State state) {
            JLPCActor.this.state = state;
        }

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
     * @param unwrappedRequest  An unwrapped request.
     * @param responseProcessor The response processor.
     * @throws Exception Any uncaught exceptions raised while processing the request.
     */
    abstract protected void processRequest(Object unwrappedRequest, ResponseProcessor responseProcessor)
            throws Exception;
}
