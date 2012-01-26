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

import org.agilewiki.jactor.Actor;
import org.agilewiki.jactor.Mailbox;
import org.agilewiki.jactor.MailboxFactory;
import org.agilewiki.jactor.ResponseProcessor;
import org.agilewiki.jactor.lpc.RequestSource;

import java.util.concurrent.ConcurrentSkipListMap;

/**
 * The API used when a request is received.
 */
public interface RequestReceiver {
    /**
     * Returns the concurrent data of the actor.
     *
     * @return The concurrent data of the actor.
     */
    ConcurrentSkipListMap<String, Object> getData();

    /**
     * Returns an actor's parent.
     *
     * @return The actor's parent, or null.
     */
    Actor getParent();

    /**
     * Returns true when the concurrent data of the parent contains the named data item.
     *
     * @param name The key for the data item.
     * @return True when the concurrent data of the parent contains the named data item.
     */
    boolean parentHasDataItem(String name);

    /**
     * Returns true when the parent has the same component.
     *
     * @return True when the parent has the same component.
     */
    boolean parentHasSameComponent();

    /**
     * Ensures that the request is processed on the appropriate thread.
     *
     * @param requestSource The originator of the request.
     * @param request       The request to be sent.
     * @param rp            The request processor.
     * @param binding       The object bound to the request class.
     * @throws Exception Any uncaught exceptions raised while processing the request.
     */
    void routeRequest(final RequestSource requestSource,
                      final Object request,
                      final ResponseProcessor rp,
                      Binding binding)
            throws Exception;

    /**
     * Returns the receiving actor's mailbox.
     *
     * @return The receiving actor's mailbox.
     */
    public Mailbox getMailbox();

    /**
     * Returns this actor.
     *
     * @return This actor.
     */
    public JBActor getThisActor();
}
