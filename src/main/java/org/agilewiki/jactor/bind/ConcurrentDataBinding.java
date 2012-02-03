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

import org.agilewiki.jactor.lpc.RequestSource;

/**
 * Binds a request class to a concurrent data item.
 * Requests are processed immediately,
 * even if the actor has an asynchronous mailbox.
 */
public class ConcurrentDataBinding extends ConcurrentMethodBinding {
    /**
     * The name of a concurrent data item.
     */
    private String name;

    /**
     * Create a ConcurrentDataBinding.
     *
     * @param name The name of a concurrent data item.
     */
    public ConcurrentDataBinding(String name) {
        this.name = name;
    }

    /**
     * <p>
     * A pure synchronous method which accesses only concurrent data structures
     * or calls other pure synchronous methods via Actor.acceptCall.
     * </p>
     *
     * @param requestReceiver The API used when a request is received.
     * @param requestSource   The originator of the request.
     * @param request         The request to be processed.
     * @return The response.
     * @throws Exception Any uncaught exceptions raised while processing the request.
     */
    @Override
    public Object concurrentProcessRequest(RequestReceiver requestReceiver, RequestSource requestSource, ConcurrentRequest request) throws Exception {
        return requestReceiver.getData().get(name);
    }
}
