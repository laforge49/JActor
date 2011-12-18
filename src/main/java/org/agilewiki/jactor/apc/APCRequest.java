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

public class APCRequest extends APCMessage {

    private RequestSource requestSource;

    private RequestProcessor requestProcessor;

    private Object data;

    private ResponseDestination responseDestination;

    private APCRequest oldRequest;

    private boolean active = true;

    public APCRequest(RequestSource requestSource,
                      RequestProcessor requestProcessor,
                      Object data,
                      ResponseDestination responseDestination) {
        this.requestSource = requestSource;
        this.requestProcessor = requestProcessor;
        this.data = data;
        this.responseDestination = responseDestination;
    }

    final public RequestSource getRequestSource() {
        return requestSource;
    }

    final public RequestProcessor getRequestProcessor() {
        return requestProcessor;
    }

    final public ExceptionHandler getExceptionHandler() {
        return requestProcessor.getExceptionHandler();
    }

    final public Object getData() {
        return data;
    }

    final public ResponseDestination getResponseDestination() {
        return responseDestination;
    }

    final public void setOldRequest(APCRequest oldRequest) {
        this.oldRequest = oldRequest;
    }

    final public APCRequest getOldRequest() {
        return oldRequest;
    }

    public boolean isActive() {
        return active;
    }

    public void inactive() {
        active = false;
    }

    public void response(BufferedEventsQueue<APCMessage> eventQueue, Object data) {
        APCResponse apcResponse = new APCResponse(data);
        apcResponse.setApcRequest(this);
        requestSource.responseFrom(eventQueue, apcResponse);
    }
}
