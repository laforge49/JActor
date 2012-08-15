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

import org.agilewiki.jactor.apc.APCMailbox;

/**
 * <p>
 * A mailbox is a lightweight thread with an inbox for incoming messages and an outbox for outgoing messages. Every
 * actor has a mailbox, though any number of actors can share the same mailbox. Actors which share the same mailbox
 * then always use the same thread and can exchange messages very quickly (about 1 billion messages per second on an
 * i5).
 * </p><p>
 * Actors typically pass messages directly from one actor to another, without having to use an inbox or outbox. But if
 * the destination actor is busy processing its own incoming messages on another thread, then the source actor puts the
 * outgoing message in its mailbox's outbox for subsequent processing when the inbox is empty. To maximize throughput,
 * mailboxes always group outgoing messages by destination actor and then sends all the messages for each destination
 * as an array list.
 * </p><p>
 * Mailboxes can be tagged as synchronous or asynchronous. Asynchronous actors are actors which have an mailbox with
 * an event queue which has been tagged as autonomous. And when an actor has a request for an asynchronous actor, the
 * request and response are always exchanged asynchronously.
 * </p>
 */
public interface Mailbox extends APCMailbox {
    /**
     * Returns the mailbox factory.
     *
     * @return The mailbox factory.
     */
    public MailboxFactory getMailboxFactory();
}
