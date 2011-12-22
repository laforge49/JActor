package org.agilewiki.jactor.lpc;

import org.agilewiki.jactor.apc.JAPCMailbox;
import org.agilewiki.jactor.apc.JAPCMessage;
import org.agilewiki.jactor.bufferedEvents.BufferedEventsDestination;
import org.agilewiki.jactor.bufferedEvents.BufferedEventsQueue;
import org.agilewiki.jactor.concurrent.ThreadManager;

import java.util.concurrent.atomic.AtomicReference;

/**
 * Implements LPCMailbox.
 */
final public class JLPCMailbox extends JAPCMailbox implements LPCMailbox {

    /**
     * Tracks which mailbox has control. If an exchange can gain control
     * over another exchange, it can send requests to it synchronously.
     */
    private final AtomicReference<LPCMailbox> atomicControl = new AtomicReference<LPCMailbox>();

    /**
     * Set to true when all requests are to be processed asynchronously.
     */
    private boolean async;

    /**
     * Create a JLPCMailbox.
     * Use this constructor when providing an implementation of BufferedEventsQueue
     * other than JABufferedEventsQueue.
     *
     * @param eventQueue The lower-level mailbox which transports messages as 1-way events.
     * @param async      Set to true when all requests are to be processed asynchronously.
     */
    public JLPCMailbox(BufferedEventsQueue<JAPCMessage> eventQueue, boolean async) {
        super(eventQueue);
        this.async = async;
    }

    /**
     * Create a JLPCMailbox.
     *
     * @param threadManager Provides a thread for processing dispatched events.
     * @param async         Set to true when all requests are to be processed asynchronously.
     */
    public JLPCMailbox(ThreadManager threadManager, boolean async) {
        super(threadManager);
        this.async = async;
    }

    /**
     * Create a JLPCMailbox.
     *
     * @param threadManager Provides a thread for processing dispatched events.
     */
    public JLPCMailbox(ThreadManager threadManager) {
        this(threadManager, false);
    }

    /**
     * Returns the controlling mailbox, or null.
     */
    @Override
    public final LPCMailbox getControllingMailbox() {
        return atomicControl.get();
    }

    /**
     * The dispatchMessages method processes any messages in the queue.
     * True is returned if any messages were actually processed.
     */
    @Override
    final public boolean dispatchEvents() {
        if (async) return super.dispatchEvents();
        boolean dispatched = false;
        if (atomicControl.compareAndSet(null, this)) {
            try {
                dispatched = super.dispatchEvents();
            } finally {
                atomicControl.set(null);
            }
        }
        return dispatched;
    }

    /**
     * Process the request immediately if possible; otherwise buffer the request for subsequent sending.
     *
     * @param destination Buffered events receiver.
     * @param request     The request to be sent.
     */
    @Override
    public final void lpcSend(BufferedEventsDestination<JAPCMessage> destination, 
                        JLPCRequest request, 
                        LPCMailbox srcMailbox) {
        /*
        if (async) {
        */
            request.setSync(false);
            apcSend(destination, request);
        /*
            return;
        }
        request.setOldRequest(getCurrentRequest());
        LPCMailbox srcControllingMailbox = srcMailbox.getControllingMailbox();
        if (getControllingMailbox() == srcControllingMailbox) {
            _sendReq(request);
            return;
        }
        request.setSync(false);
        apcSend(destination, request);
        if (!atomicControl.compareAndSet(null, srcControllingMailbox)) {
            request.setSync(false);
            apcSend(destination, request);
            return;
        }
        try {
            _sendReq(request);
        } finally {
            atomicControl.set(null);
        }
        if (!isEmpty()) sendRem(srcMailbox);
        */
    }
    
    private final void _sendReq(JLPCRequest request) {
        request.setSync(true);
        //todo
    }

    private final void sendRem(LPCMailbox srcMailbox) {
        //todo
    }
}
