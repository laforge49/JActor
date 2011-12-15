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
 * The apc package implements Asynchronous Procedure Calls (APC), or two-way messaging.
 * A message is treated as either a request or a response, where for every request there is
 * exactly one response.
 * </p>
 * <p>
 * Actors traditionally use one-way messaging, which can be difficult to understand when the code
 * of an actor becomes large. The apc package inst5ead uses messages to implement something closer
 * to a method call. It also has the advantage of implicitly implementing flow control--which tends
 * to result in systems that behave well under load.
 * </p>
 */

package org.agilewiki.jactor.apc;
