package org.agilewiki.jactor.lpc;

import org.agilewiki.jactor.apc.APCMailbox;
import org.agilewiki.jactor.apc.JAPCMessage;
import org.agilewiki.jactor.apc.JAPCRequest;
import org.agilewiki.jactor.bufferedEvents.BufferedEventsDestination;

import java.util.ArrayList;

/**
 * Implements LPCMailbox.
 */
public class JLPCMailbox implements LPCMailbox {
    
    private APCMailbox mailbox;

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
        //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * Set the initial capacity for buffered outgoing messages.
     *
     * @param initialBufferCapacity The initial capacity for buffered outgoing messages.
     */
    @Override
    public void setInitialBufferCapacity(int initialBufferCapacity) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * Buffer the request for subsequent sending.
     *
     * @param destination Buffered events receiver.
     * @param request     The request to be sent.
     */
    @Override
    public void send(BufferedEventsDestination<JAPCMessage> destination, JAPCRequest request) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * Return the response for processing.
     *
     * @param unwrappedResponse
     */
    @Override
    public void response(Object unwrappedResponse) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * The isEmpty method returns true when there are no pending messages,
     * though the results may not always be correct due to concurrency issues.
     */
    @Override
    public boolean isEmpty() {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * The dispatchMessages method processes any messages in the queue.
     * True is returned if any messages were actually processed.
     */
    @Override
    public boolean dispatchEvents() {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * The putBufferedEvents method adds events to be processed.
     *
     * @param bufferedEvents The events to be processed.
     */
    @Override
    public void putBufferedEvents(ArrayList<JAPCMessage> bufferedEvents) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
