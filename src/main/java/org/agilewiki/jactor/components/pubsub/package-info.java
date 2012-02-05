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
 * <p>
 *     Publish/Subscribe using 2-way messaging, with the advantage that a publish
 *     operation returns no result until the message has been processed by
 *     all subscribers.
 *     There is no defined order for publishing requests to subscribers.
 *     And like JAParallel, requests are published in parallel.
 * </p>
 * <p>
 *     To demonstrate publish/subscribe, we first need a request which will be published.
 * </p>
 * <pre>
 *     final public class PSRequest {}
 * </pre>
 * <p>
 *     Now we need an actor that will receive the request, i.e. a subscriber.
 * </p>
 * <pre>
 *     public class Subscriber extends JLPCActor {
 *         public Subscriber(Mailbox mailbox) {
 *             super(mailbox);
 *         }
 *
 *         protected void processRequest(Object request, ResponseProcessor rp) throws Exception {
 *             System.err.println("Got request.");
 *             rp.process(null);
 *         }
 *     }
 * </pre>
 * <p>
 *     Test code.
 * </p>
 * <pre>
 *     MailboxFactory mailboxFactory = JAMailboxFactory.newMailboxFactory(1);
 *     Mailbox mailbox = mailboxFactory.createMailbox();
 *     Actor publisher = new JCActor(mailbox);
 *     JAFuture future = new JAFuture();
 *     future.call(publisher, new Include(PubSub.class));
 *     Actor subscriber1 = new Subscriber(mailbox);
 *     Actor subscriber2 = new Subscriber(mailbox);
 *     future.call(publisher, new Subscribe(subscriber1));
 *     future.call(publisher, new Subscribe(subscriber2));
 *     future.send(publisher, new Publish(new PSRequest()));
 *     future.call(publisher, new Unsubscribe(subscriber1));
 *     future.send(publisher, new Publish(new PSRequest()));
 * </pre>
 * <p>
 *     Output.
 * </p>
 * <pre>
 *     Got request.
 *     Got request.
 *     Got request.
 * </pre>
 */
package org.agilewiki.jactor.components.pubsub;
