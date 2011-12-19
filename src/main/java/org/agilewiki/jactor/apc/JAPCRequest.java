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
package org.agilewiki.jactor.apc;

import org.agilewiki.jactor.bufferedEvents.BufferedEventsQueue;

/**
 * Requests sent to a JAPCMailbox are wrapped by an JAPCRequest.
 */
public class JAPCRequest extends JAPCMessage {

    /**
     * The target of the response.
     * An anonymous object in the JAPCActor or JAPCFuture
     * that originated the request. Used in response processing.
     */
    private APCRequestSource requestSource;

    /**
     * An anonymous object in the JAPCActor that is the target of the JAPCRequest.
     */
    private APCRequestProcessor apcRequestProcessor;

    /**
     * The unwrapped request that was sent to a JAPCActor.
     */
    private Object unwrappedRequest;

    /**
     * An object used to process an unwrapped response.
     */
    private ResponseProcessor responseProcessor;

    /**
     * The JAPCRequest that was being processed when this request was created.
     */
    private JAPCRequest oldRequest;

    /**
     * Initially true, active is set to false when a response is sent.
     * Used to prevent multiple responses to a request.
     */
    private boolean active = true;

    /**
     * Create an JAPCRequest.
     *
     * @param requestSource       The target of the response.
     * @param apcRequestProcessor The target of the request.
     * @param unwrappedRequest    The wrapped request.
     * @param responseProcessor   Processes the response.
     */
    public JAPCRequest(APCRequestSource requestSource,
                       APCRequestProcessor apcRequestProcessor,
                       Object unwrappedRequest,
                       ResponseProcessor responseProcessor) {
        this.requestSource = requestSource;
        this.apcRequestProcessor = apcRequestProcessor;
        this.unwrappedRequest = unwrappedRequest;
        this.responseProcessor = responseProcessor;
    }

    /**
     * Returns the apcRequestProcessor.
     *
     * @return The apcRequestProcessor.
     */
    final public APCRequestProcessor getApcRequestProcessor() {
        return apcRequestProcessor;
    }

    /**
     * Returns the exceptionHandler.
     *
     * @return The exceptionHandler.
     */
    final public ExceptionHandler getExceptionHandler() {
        return apcRequestProcessor.getExceptionHandler();
    }

    /**
     * Returns the unwrapped request.
     *
     * @return The unwrapped request.
     */
    final public Object getUnwrappedRequest() {
        return unwrappedRequest;
    }

    /**
     * Returns the responseProcessor.
     *
     * @return The responseProcessor.
     */
    final public ResponseProcessor getResponseProcessor() {
        return responseProcessor;
    }

    /**
     * Sets oldREquest to the request being processed when this request
     * was created.
     *
     * @param oldRequest The request being processed when this request was created.
     */
    final public void setOldRequest(JAPCRequest oldRequest) {
        this.oldRequest = oldRequest;
    }

    /**
     * Returns the request that was being processed when this request
     * was created.
     *
     * @return The request that was being processed when this request
     *         was created.
     */
    final public JAPCRequest getOldRequest() {
        return oldRequest;
    }

    /**
     * Returns true if no response has been returned.
     *
     * @return Is true when no response has been returned.
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Sets active to false--a response has been returned.
     */
    public void inactive() {
        active = false;
    }

    /**
     * Enqueue a response to be sent when there are no more incoming messages to be processed.
     *
     * @param eventQueue        The internal queue used by JAPCMailbox.
     * @param unwrappedResponse The unwrapped response.
     */
    public void response(BufferedEventsQueue<JAPCMessage> eventQueue, Object unwrappedResponse) {
        JAPCResponse japcResponse = new JAPCResponse(unwrappedResponse);
        japcResponse.setJAPCRequest(this);
        requestSource.responseFrom(eventQueue, japcResponse);
    }
}
