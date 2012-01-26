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

/**
 * <h2>
 *     An Introduction to Request Class Binding
 * </h2>
 * <p>
 *     A JBActor maintains a table of Binding objects, keyed by the class names of
 *     the requests it services. A JBActor may also have a parent actor, to which
 *     it routes unrecognized requests. (Assigning a parent to an actor is a kind
 *     of dependency injection.)
 * </p>
 * <p>
 *     A JBActor also has a ConcurrentSkipListMap of application data (Objects). This
 *     map is used primarily by the JCActor to hold its Components, though its use
 *     is not restricted to Components.
 * </p>
 * <p>
 *     Like JLPCActor, JBActor partitions much of its API into interfaces, including the
 *     RequestProcessor and RequestSource. JBActor introduces 2 additional Interfaces:
 *     Internals and RequestProcessor. This was done partly for clarity, the API being
 *     organized by usage, and partly for thread safety--many of the method of the Internals
 *     interface are unsafe to call except from a thread which has "control" of the actor.
 * </p>
 * <h2>
 *     The Binding Class
 * </h2>
 * <p>
 *     The Binding class is key to understanding how JBActor works. Request messages which can be
 *     handled by a JBActor have been bound by the name of the class of the request to a
 *     Binding object provided by the application logic. The Binding class has two methods:
 *     acceptRequest and processRequest.
 * </p>
 * <p>
 *     The Binding.acceptRequest method is called when a request message is sent to a JBActor,
 *     and executes on the same thread as the code which sent the message--which limits what
 *     can be done safely. Sometimes the request can be processed immediately using only
 *     immutable or concurrent data. Sometimes the the request can be forwarded to another actor
 *     by calling the acceptRequest method of that other actor. Otherwise the RequestReceiver.routeRequest
 *     method is called, which then ensures that an appropriate thread is used to call Binding.processRequest.
 * </p>
 * <p>
 *     The Binding.processRequest method, like JLPCActor.processRequest, is always called using a
 *     thread which has exclusive control of the actor. But unlike JLPCActor.processRequest, the Binding.processRequest
 *     method is always called with the same class of request message. And, depending on the logic of the
 *     Binding.acceptRequest method, the Binding.processRequest method may not be called for some types of requests.
 * </p>
 */
package org.agilewiki.jactor.bind;