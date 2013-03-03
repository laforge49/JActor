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
import org.agilewiki.jactor.events.EventQueue;
import org.agilewiki.jactor.simpleMachine._SMBuilder;

import java.util.ArrayList;

/**
 * <p>
 * An actor which implements Local Procedure Calls (LPC)
 * and mostly works synchronously.
 * Actors need to implement the processRequest method.
 * </p>
 * <pre>
 * public class Multiply {
 *     public int a;
 *     public int b;
 * }
 *
 * public class Multiplier extends JLPCActor {
 *
 *     public Multiplier(Mailbox mailbox) {
 *         super(mailbox);
 *     }
 *
 *     protected void processRequest(Object req, RP rp)
 *             throws Exception {
 *         Multiply m = (Multiply) req;
 *         rp.process(new Integer(m.a * m.b));
 *     }
 * }
 * </pre>
 */
abstract public class JLPCActor implements TargetActor, RequestProcessor, RequestSource {
    /**
     * The inbox and outbox of the actor.
     */
    private Mailbox mailbox;

    /**
     * The parent actor, for dependency injection.
     */
    private JLPCActor parent;

    /**
     * Initialize a degraded LiteActor
     */
    public void initialize() throws Exception {
        initialize(null, null);
    }

    /**
     * Initialize a degraded LiteActor
     *
     * @param parent The parent actor.
     */
    public void initialize(Actor parent) throws Exception {
        initialize(null, parent);
    }

    /**
     * Initialize a LiteActor
     *
     * @param mailbox A mailbox which may be shared with other actors.
     */
    public void initialize(final Mailbox mailbox) throws Exception {
        initialize(mailbox, null);
    }

    /**
     * Initialize a LiteActor
     *
     * @param mailbox A mailbox which may be shared with other actors.
     * @param parent  The parent actor.
     */
    public void initialize(final Mailbox mailbox, Actor parent) throws Exception {
        if (this.mailbox != null || this.parent != null)
            throw new IllegalStateException("already initialized");
        this.mailbox = mailbox;
        this.parent = (JLPCActor) parent;
    }

    /**
     * Returns the actor's parent.
     *
     * @return The actor's parent, or null.
     */
    @Override
    final public JLPCActor getParent() {
        return parent;
    }

    public JLPCActor getMatch(Class targetClass) {
        if (targetClass.isInstance(this))
            return this;
        return getAncestor(targetClass);
    }

    /**
     * Returns A matching ancestor from the parent chain.
     *
     * @param targetClass A class which the ancestor is an instanceof.
     * @return The matching ancestor, or null.
     */
    @Override
    final public JLPCActor getAncestor(Class targetClass) {
        if (parent == null)
            return null;
        if (targetClass.isInstance(parent))
            return parent;
        return parent.getAncestor(targetClass);
    }

    /**
     * Returns the exception handler.
     *
     * @return The exception handler.
     */
    @Override
    final public ExceptionHandler getExceptionHandler() {
        return mailbox.getExceptionHandler();
    }

    /**
     * Assign an exception handler.
     *
     * @param exceptionHandler The exception handler.
     */
    @Override
    final public void setExceptionHandler(final ExceptionHandler exceptionHandler) {
        mailbox.setExceptionHandler(exceptionHandler);
    }

    /**
     * A notification that there are incoming requests and responses that are ready for processing.
     */
    @Override
    final public void haveEvents() {
        mailbox.dispatchEvents();
    }

    /**
     * Returns the actor's mailbox.
     *
     * @return The actor's mailbox.
     */
    @Override
    final public Mailbox getMailbox() {
        return mailbox;
    }

    /**
     * Enqueues the response in the responder's outbox.
     *
     * @param eventQueue   The responder's outbox.
     * @param japcResponse The wrapped response to be enqueued.
     */
    @Override
    final public void responseFrom(final BufferedEventsQueue<JAMessage> eventQueue,
                                   final JAResponse japcResponse) {
        eventQueue.send(mailbox, japcResponse);
    }

    /**
     * Sends a request to a mailbox.
     *
     * @param destination The mailbox which is to receive the request.
     * @param japcRequest The wrapped request to be sent.
     */
    @Override
    final public void send(final BufferedEventsDestination<JAMessage> destination,
                           final JARequest japcRequest) {
        mailbox.send(destination, japcRequest);
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
    final public void acceptRequest(APCRequestSource apcRequestSource,
                                    Request request,
                                    RP rp)
            throws Exception {
        RequestSource rs = (RequestSource) apcRequestSource;
        Mailbox sourceMailbox = rs.getMailbox();
        if (sourceMailbox == mailbox) {
            syncSend(rs, request, rp);
            return;
        }
        if (sourceMailbox == null) {
            asyncSend(rs, request, rp);
            return;
        }
        acceptOtherRequest(sourceMailbox, rs, request, rp);
    }

    private void acceptOtherRequest(
            Mailbox sourceMailbox,
            RequestSource rs,
            Request request,
            RP rp) throws Exception {
        EventQueue<ArrayList<JAMessage>> eventQueue = mailbox.getEventQueue();
        EventQueue<ArrayList<JAMessage>> srcController = sourceMailbox.getEventQueue().getController();
        if (eventQueue.getController() == srcController) {
            syncSend(rs, request, rp);
            return;
        }
        if (!eventQueue.acquireControl(srcController)) {
            asyncSend(rs, request, rp);
            return;
        }
        try {
            syncSend(rs, request, rp);
        } finally {
            mailbox.dispatchEvents();
            mailbox.sendPendingMessages();
            eventQueue.relinquishControl();
        }
    }

    private void asyncSend(final RequestSource rs,
                           final Request request,
                           final RP rp)
            throws Exception {
        AsyncRequest asyncRequest = new AsyncRequest(
                rs,
                this,
                request,
                rp,
                mailbox);
        rs.send(mailbox, asyncRequest);
    }

    private void syncSend(RequestSource rs,
                          Request request,
                          RP rp)
            throws Exception {
        SyncRequest syncRequest = new SyncRequest(
                rs,
                JLPCActor.this,
                request,
                rp,
                mailbox);
        mailbox.setCurrentRequest(syncRequest);
        try {
            setExceptionHandler(null);
            request.processRequest(this, syncRequest);
            if (!syncRequest.sync) {
                syncRequest.async = true;
                syncRequest.restoreSourceMailbox();
            }
        } catch (TransparentException tx) {
            throw (Exception) tx.getCause();
        } catch (Exception ex) {
            if (!syncRequest.sync)
                syncRequest.restoreSourceMailbox();
            throw ex;
        }
    }

    /**
     * Wraps and enqueues an unwrapped request in the requester's inbox.
     *
     * @param apcRequestSource The originator of the request.
     * @param request          The request to be sent.
     * @throws Exception Any uncaught exceptions raised while processing the request.
     */
    @Override
    final public void acceptEvent(APCRequestSource apcRequestSource,
                                  Request request)
            throws Exception {
        RequestSource rs = (RequestSource) apcRequestSource;
        ExceptionHandler sourceExceptionHandler = rs.getExceptionHandler();
        Mailbox sourceMailbox = rs.getMailbox();
        if (sourceMailbox == mailbox) {
            syncSendEvent(rs, request, sourceExceptionHandler);
            return;
        }
        if (sourceMailbox == null) {
            asyncSendEvent(rs, request);
            return;
        }
        EventQueue<ArrayList<JAMessage>> eventQueue = mailbox.getEventQueue();
        EventQueue<ArrayList<JAMessage>> srcController = sourceMailbox.getEventQueue().getController();
        if (eventQueue.getController() == srcController) {
            syncSendEvent(rs, request, sourceExceptionHandler);
            return;
        }
        if (!eventQueue.acquireControl(srcController)) {
            asyncSendEvent(rs, request);
            return;
        }
        try {
            syncSendEvent(rs, request, sourceExceptionHandler);
        } finally {
            mailbox.dispatchEvents();
            mailbox.sendPendingMessages();
            eventQueue.relinquishControl();
        }
    }

    /**
     * Process a request asynchronously.
     *
     * @param rs      The source of the request.
     * @param request The request.
     */
    private void asyncSendEvent(RequestSource rs, Request request) {
        JAEventRequest jaRequest = new JAEventRequest(rs, this, request, mailbox);
        rs.send(mailbox, jaRequest);
    }

    /**
     * Process a request from another mailbox synchronously.
     *
     * @param rs                     The source of the request.
     * @param request                The request.
     * @param sourceExceptionHandler Exception handler of the source actor.
     */
    private void syncSendEvent(RequestSource rs,
                               Request request,
                               ExceptionHandler sourceExceptionHandler) {
        Mailbox oldSourceMailbox = rs.getMailbox();
        JARequest oldSourceRequest = oldSourceMailbox.getCurrentRequest();
        JAEventRequest jaRequest = new JAEventRequest(rs, this, request, mailbox);
        mailbox.setCurrentRequest(jaRequest);
        try {
            setExceptionHandler(null);
            request.processRequest(this, JANoResponse.nrp);
        } catch (Exception ex) {
            ExceptionHandler eh = getExceptionHandler();
            if (eh != null)
                try {
                    eh.process(ex);
                    return;
                } catch (Exception x) {
                    getMailboxFactory().eventException(request, x);
                }
            else {
                getMailboxFactory().eventException(request, ex);
            }
        }
        oldSourceMailbox.setCurrentRequest(oldSourceRequest);
        oldSourceMailbox.setExceptionHandler(sourceExceptionHandler);
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
                              final Request request,
                              final RP rp)
            throws Exception {
        actor.acceptRequest(this, request, rp);
    }

    /**
     * Send a request to another actor and discard any response.
     *
     * @param actor   The target actor.
     * @param request The request.
     */
    final protected void sendEvent(Actor actor, Request request) {
        try {
            send(actor, request, JANoResponse.nrp);
        } catch (Exception ex) {
            throw new UnsupportedOperationException("Unexpected exception", ex);
        }
    }

    /**
     * Creates a _SMBuilder.
     */
    final public class SMBuilder extends _SMBuilder {
        @Override
        final public void send(Actor actor, Request request, RP rp)
                throws Exception {
            JLPCActor.this.send(actor, request, rp);
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
     * Returns true when the concurrent data of the actor, or its parent, contains the named data item.
     *
     * @param name The key for the data item.
     * @return True when the concurrent data of the actor, or its parent, contains the named data item.
     */
    @Override
    final public boolean hasDataItem(String name) {
        if (parent == null)
            return false;
        return parent.hasDataItem(name);
    }
}

final class SyncRequest extends JARequest {
    /**
     * Set true when a response is received synchronously.
     */
    public boolean sync;

    /**
     * Set true when a response is received asynchronously.
     */
    public boolean async;

    public SyncRequest(RequestSource requestSource,
                       JLPCActor destinationActor,
                       Request unwrappedRequest,
                       RP rp,
                       Mailbox mailbox) {
        super(requestSource, destinationActor, unwrappedRequest, rp, mailbox);
    }

    @Override
    public void processResponse(Object response) throws Exception {
        if (!async) {
            sync = true;
            if (!isActive()) {
                return;
            }
            inactive();
            JARequest oldCurrent = mailbox.getCurrentRequest();
            ExceptionHandler oldExceptionHandler = mailbox.getExceptionHandler();
            restoreSourceMailbox();
            if (response instanceof Exception) {
                mailbox.setCurrentRequest(oldCurrent);
                mailbox.setExceptionHandler(oldExceptionHandler);
                throw (Exception) response;
            }
            try {
                rp.processResponse(response);
            } catch (Exception e) {
                throw new TransparentException(e);
            }
            mailbox.setCurrentRequest(oldCurrent);
            mailbox.setExceptionHandler(oldExceptionHandler);
        } else {
            if (response instanceof Exception) {
                Exception ex = (Exception) response;
                ExceptionHandler exceptionHandler = mailbox.getExceptionHandler();
                if (exceptionHandler != null) {
                    try {
                        exceptionHandler.process(ex);
                        return;
                    } catch (Exception x) {
                        ex = x;
                    }
                    mailbox.response(this, ex);
                }
            }
            mailbox.response(this, response);
            return;
        }
        reset();
    }
}

final class AsyncRequest extends JARequest {
    public AsyncRequest(RequestSource requestSource,
                        JLPCActor destinationActor,
                        Request unwrappedRequest,
                        RP rp,
                        Mailbox mailbox) {
        super(
                requestSource,
                destinationActor,
                unwrappedRequest,
                rp,
                mailbox);
    }

    @Override
    public void processResponse(Object response) throws Exception {
        if (!isActive()) {
            return;
        }
        JARequest old = mailbox.getCurrentRequest();
        mailbox.setCurrentRequest(this);
        mailbox.response(this, response);
        mailbox.setCurrentRequest(old);
    }
}

final class JAEventRequest extends JARequest {
    public JAEventRequest(RequestSource requestSource,
                          JLPCActor destinationActor,
                          Request unwrappedRequest,
                          Mailbox mailbox) {
        super(
                requestSource,
                destinationActor,
                unwrappedRequest,
                null,
                mailbox);
    }

    @Override
    public void processResponse(Object response) throws Exception {
        reset();
    }

    /**
     * Returns true when no response is expected.
     *
     * @return True.
     */
    @Override
    public boolean isEvent() {
        return true;
    }
}
