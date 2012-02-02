/*
 * Copyright 2012 Bill La Forge
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

import org.agilewiki.jactor.ResponseProcessor;
import org.agilewiki.jactor.lpc.RequestSource;

/**
 * Binds a request class to a purely synchronous method.
 */
abstract public class MethodBinding extends SyncBinding {
    /**
     * <p>
     * Process an incoming request.
     * Operates in the sender's thread, so only concurrent data structures can be updated safely.
     * </p><p>
     * The Binding.processRequest should be invoked indirectly, by calling Binding.internals.acceptRequest.
     * The acceptRequest method will then be safely invoked under the appropriate thread.
     * </p><p>
     * The send method is also not safe, but you can call Actor.acceptRequest to forward a
     * request to another actor.
     * </p>
     *
     * @param requestReceiver The API used when a request is received.
     * @param requestSource   The originator of the request.
     * @param request         The request to be sent.
     * @param rp              The request processor.
     * @throws Exception Any uncaught exceptions raised while processing the request.
    */
    @Override
    public void acceptRequest(RequestReceiver requestReceiver, 
                              RequestSource requestSource, 
                              Object request, 
                              ResponseProcessor rp) 
            throws Exception {
        rp.process(method(request));
    }

    /**
     * A purely synchronous method which accesses only concurrent data structures.
     *
     * @param request The request to be processed.
     * @return The response.
     * @throws Exception Any uncaught exceptions raised while processing the request.
     */
    abstract public Object method(Object request) throws Exception;
}
