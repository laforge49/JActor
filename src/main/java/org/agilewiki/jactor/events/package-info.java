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
 * The events package supports the dispatching of one-way messages (events).
 * </p>
 * <p>
 * A simple echo test takes 123 nanoseconds to send a message
 * when running with a single thread and 252 nanoseconds when running with multiple threads.
 * </p>
 * <p>
 * A dual echo test takes 113 nanoseconds to send a message
 * when running with a single thread, 210 nanoseconds when running with 2 threads,
 * 223 nanoseconds when running with 4 threads and 225 nanoseconds when running
 * with 8 threads.
 * </p>
 * <p>
 * Tests were done on an Intel Core i5 CPU M 540 @ 2.53GHz, which has 4 hardware threads.
 * The times reported were best run in 5.
 * </p>
 */
package org.agilewiki.jactor.events;
