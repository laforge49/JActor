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
package org.agilewiki.jactor.bind;

import org.agilewiki.jactor.*;

import java.util.concurrent.ConcurrentSkipListMap;

/**
 * The API used when processing a request.
 */
public interface Internals {
    /**
     * Internal concurrent data of the actor.
     */
    public ConcurrentSkipListMap<String, Object> getData();

    /**
     * Add a binding to the actor.
     *
     * @param requestClassName The class name of the request.
     * @param binding          The binding.
     * @throws IllegalStateException Thrown if there is already a binding for the class.
     */
    void bind(String requestClassName, Binding binding) throws IllegalStateException;

    /**
     * Returns a binding.
     *
     * @param request The request.
     * @return The binding, or null.
     */
    public Binding getBinding(Object request);

    /**
     * Send a constrained request.
     *
     * @param targetActor The target actor.
     * @param request     The request.
     * @return The response.
     * @throws Exception Any uncaught exceptions raised while processing the request.
     */
    public Object call(Actor targetActor, ConstrainedRequest request)
            throws Exception;

    /**
     * Send a request to another actor.
     *
     * @param actor   The target actor.
     * @param request The request.
     * @param rp      The response processor.
     * @throws Exception Any uncaught exceptions raised while processing the request.
     */
    public void send(final Actor actor,
                     final Object request,
                     final ResponseProcessor rp)
            throws Exception;

    /**
     * Send a request to another actor and discard any response.
     *
     * @param actor   The target actor.
     * @param request The request.
     */
    public void sendEvent(Actor actor, Object request);

    /**
     * Returns the exception handler.
     *
     * @return The exception handler.
     */
    public ExceptionHandler getExceptionHandler();

    /**
     * Assign an exception handler.
     *
     * @param exceptionHandler The exception handler.
     */
    public void setExceptionHandler(final ExceptionHandler exceptionHandler);

    /**
     * Returns this actor.
     *
     * @return This actor.
     */
    public JBActor getThisActor();
}
