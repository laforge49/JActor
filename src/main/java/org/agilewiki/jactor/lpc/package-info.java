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
 * The lpc package implements Local Procedure Calls (LPC), which are similar to APCs, except
 * that they mostly operate synchronously. The effect is higher performance, especially when
 * calls are made one at a time rather than in bursts--which is typical of general programming.
 * But the big news is that LPCs are fast enough that an "actors everywhere" approach becomes
 * reasonable, and it is now relatively easy to develop software that can fully utilize any
 * number of hardware threads.
 * </p>
 * <p>
 * LPCMailboxes are now first class objects which can be shared by LPCActors. The result is
 * even better performance (1.2 nanoseconds per message), as procedure calls between actors
 * with the same mailbox do not use request or response wrappers.
 * </p>
 * <p>
 * Now there are times when, to be able to make good use of the available hardware threads,
 * asynchronous procedure calls are needed. This is achieved by flagging a LPCMailbox as
 * asynchronous. Procedure calls to an actor with an asynchronous mailbox are always asynchronous--
 * except when both actors share the same mailbox.
 * </p>
 */

package org.agilewiki.jactor.lpc;
