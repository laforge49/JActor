package org.agilewiki.jactor.lpc;

import org.agilewiki.jactor.apc.APCMailbox;
import org.agilewiki.jactor.apc.JAPCMailbox;
import org.agilewiki.jactor.apc.JAPCMessage;
import org.agilewiki.jactor.apc.JAPCRequest;
import org.agilewiki.jactor.bufferedEvents.BufferedEventsDestination;
import org.agilewiki.jactor.concurrent.ThreadManager;

import java.util.ArrayList;

/**
 * Implements LPCMailbox.
 */
public class JLPCMailbox implements LPCMailbox {

    /**
     * Set to true when all requests are to be processed asynchronously.
     */
    private boolean async;

    /**
     * The lower-level mailbox which actually transports the messages.
     */
    private APCMailbox mailbox;

    /**
     * Create a JLPCMailbox.
     * Use this constructor when providing an implementation of APCMailbox
     * other than JAPCMailbox.
     *
     * @param mailbox The lower-level mailbox which actually transports the messages.
     * @param async Set to true when all requests are to be processed asynchronously.
     */
    public JLPCMailbox(APCMailbox mailbox, boolean async) {
        this.async = async;
        this.mailbox = mailbox;
    }

    /**
     * Create a JLPCMailbox.
     *
     * @param threadManager Provides a thread for processing dispatched events.
     * @param async Set to true when all requests are to be processed asynchronously.
     */
    public JLPCMailbox(ThreadManager threadManager, boolean async) {
        this(new JAPCMailbox(threadManager), async);
    }

    /**
     * Create a JLPCMailbox.
     *
     * @param threadManager Provides a thread for processing dispatched events.
     */
    public JLPCMailbox(ThreadManager threadManager) {
        this(new JAPCMailbox(threadManager), false);
    }

    /**
     * Returns the request message being processed.
     *
     * @return The request message being processed.
     */
    public JAPCRequest getCurrentRequest() {
        return mailbox.getCurrentRequest();
    }

    /**
     * Assigns the request message to be processed.
     *
     * @param currentRequest The request message being processed.
     */
    public void setCurrentRequest(JAPCRequest currentRequest) {
        mailbox.setCurrentRequest(currentRequest);
    }
    
    /**
     * Send any pending messages.
     */
    @Override
    public void sendPendingMessages() {
        mailbox.sendPendingMessages();
    }

    /**
     * Set the initial capacity for buffered outgoing messages.
     *
     * @param initialBufferCapacity The initial capacity for buffered outgoing messages.
     */
    @Override
    public void setInitialBufferCapacity(int initialBufferCapacity) {
        mailbox.setInitialBufferCapacity(initialBufferCapacity);
    }

    /**
     * Buffer the request for subsequent sending.
     *
     * @param destination Buffered events receiver.
     * @param request     The request to be sent.
     */
    @Override
    public void send(BufferedEventsDestination<JAPCMessage> destination, JAPCRequest request) {
        mailbox.send(destination, request);
    }

    /**
     * Return the response for processing.
     *
     * @param unwrappedResponse
     */
    @Override
    public void response(Object unwrappedResponse) {
        mailbox.response(unwrappedResponse);
    }

    /**
     * The isEmpty method returns true when there are no pending messages,
     * though the results may not always be correct due to concurrency issues.
     */
    @Override
    public boolean isEmpty() {
        return mailbox.isEmpty();
    }

    /**
     * The dispatchMessages method processes any messages in the queue.
     * True is returned if any messages were actually processed.
     */
    @Override
    public boolean dispatchEvents() {
        return mailbox.dispatchEvents();
    }

    /**
     * The putBufferedEvents method adds events to be processed.
     *
     * @param bufferedEvents The events to be processed.
     */
    @Override
    public void putBufferedEvents(ArrayList<JAPCMessage> bufferedEvents) {
        mailbox.putBufferedEvents(bufferedEvents);
    }
}
