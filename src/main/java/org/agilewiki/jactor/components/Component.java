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
package org.agilewiki.jactor.components;

import org.agilewiki.jactor.Actor;
import org.agilewiki.jactor.ExceptionHandler;
import org.agilewiki.jactor.MailboxFactory;
import org.agilewiki.jactor.ResponseProcessor;
import org.agilewiki.jactor.bind.Binding;
import org.agilewiki.jactor.bind.JBActor;
import org.agilewiki.jactor.stateMachine._SMBuilder;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * Part of a composite actor.
 */
public class Component {
    /**
     * The JBActor's internals.
     */
    protected JBActor.Internals internals;

    /**
     * Returns a list of Includes for inclusion in the actor.
     *
     * @return A list of classes for inclusion in the actor.
     */
    public ArrayList<Include> includes() {
        return null;
    }

    /**
     * Initialize the component after all its includes have been processed.
     * The response must always be null;
     *
     * @param internals The JBActor's internals.
     * @throws Exception Any exceptions thrown during the open.
     */
    public void open(JBActor.Internals internals, ResponseProcessor rp)
            throws Exception {
        this.internals = internals;
        rp.process(null);
    }

    /**
     * Close any files or sockets opened by the component.
     *
     * @throws Exception All exceptions thrown will be ignored.
     */
    public void close() throws Exception {
    }

    /**
     * Returns the concurrent data of the actor.
     *
     * @return The concurrent data of the actor.
     */
    final protected ConcurrentSkipListMap<String, Object> getData() {
        return internals.data;
    }

    /**
     * Returns true when the concurrent data of the actor, or its parent, contains the named data item.
     *
     * @param name The key for the data item.
     * @return True when the concurrent data of the actor, or its parent, contains the named data item.
     */
    final protected boolean hasDataItem(String name) {
        return getThisActor().hasDataItem(name);
    }

    /**
     * Returns true when the concurrent data of the parent contains the named data item.
     *
     * @param name The key for the data item.
     * @return True when the concurrent data of the parent contains the named data item.
     */
    final protected boolean parentHasDataItem(String name) {
        return internals.parentHasDataItem(name);
    }

    /**
     * Returns true when the parent has the same component.
     *
     * @return True when the parent has the same component.
     */
    final protected boolean parentHasSameComponent() {
        return parentHasDataItem(getClass().getName());
    }

    /**
     * Add a binding to the actor.
     *
     * @param requestClassName The class name of the request.
     * @param binding          The binding.
     */
    final protected void bind(String requestClassName, Binding binding) {
        internals.bind(requestClassName, binding);
    }

    /**
     * Send a request to another actor.
     *
     * @param actor   The target actor.
     * @param request The request.
     * @param rp      The response processor.
     * @throws Exception Any uncaught exceptions raised while processing the request.
     */
    final protected void send(final Actor actor,
                              final Object request,
                              final ResponseProcessor rp)
            throws Exception {
        internals.send(actor, request, rp);
    }

    /**
     * Returns the mailbox factory.
     *
     * @return The mailbox factory.
     */
    final protected MailboxFactory getMailboxFactory() {
        return internals.getMailboxFactory();
    }

    /**
     * Returns the exception handler.
     *
     * @return The exception handler.
     */
    final protected ExceptionHandler getExceptionHandler() {
        return internals.getExceptionHandler();
    }

    /**
     * Assign an exception handler.
     *
     * @param exceptionHandler The exception handler.
     */
    final protected void setExceptionHandler(final ExceptionHandler exceptionHandler) {
        internals.setExceptionHandler(exceptionHandler);
    }

    /**
     * Creates a _SMBuilder.
     */
    final protected class SMBuilder extends _SMBuilder {
        @Override
        final public void send(Actor actor, Object request, ResponseProcessor rp)
                throws Exception {
            send(actor, request, rp);
        }
    }

    /**
     * Returns the actor's parent.
     *
     * @return The actor's parent, or null.
     */
    final protected Actor getParent() {
        return internals.getParent();
    }

    /**
     * Returns this actor.
     *
     * @return This actor.
     */
    final protected JBActor getThisActor() {
        return internals.getThisActor();
    }
}
