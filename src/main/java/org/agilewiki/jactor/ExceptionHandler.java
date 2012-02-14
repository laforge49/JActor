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
package org.agilewiki.jactor;

/**
 * <p>
 * As responses can be received either synchronously or asynchronously,
 * control flow can be somewhat obscure and the code to catch exceptions
 * can be error prone. Use of an exception handler makes life simpler.
 * </p>
 * <p>
 * To assign an exception handler, call the setExceptionHandler method in the
 * constructor of an actor.
 * </p>
 * <p>
 * If an exception occurs when processing a request, and no exception handler
 * has been assigned, the exception is passed to the exception handler of
 * the actor which sent the request, recursively.
 * </p>
 * <pre>
 * public class Doer extends JLPCActor {
 *     public Doer(Mailbox mailbox) {
 *         super(mailbox);
 *     }
 *
 *     protected void processRequest(final Object request, final RP rp)
 *             throws Exception {
 *         setExceptionHandler(new ExceptionHandler() {
 *             public void process(Exception exception) throws Exception {
 *                 System.out.println("Exception caught by Doer");
 *                 rp.process(null);
 *             }
 *         });
 *         if (request instanceof T1) {
 *             throw new Exception("Exception thrown in request processing");
 *         } else {
 *             rp.process(request);
 *         }
 *     }
 * }
 * </pre>
 */
public interface ExceptionHandler {
    /**
     * Process an exception.
     *
     * @param exception The exception to be processed.
     * @throws Exception Any uncaught exceptions raised while processing the exception.
     */
    void process(Exception exception) throws Exception;
}
