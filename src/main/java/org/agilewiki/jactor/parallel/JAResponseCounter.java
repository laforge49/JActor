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
package org.agilewiki.jactor.parallel;

import org.agilewiki.jactor.RP;

/**
 * Returns a response only when the expected number of responses are received.
 */
final public class JAResponseCounter extends RP {
    /**
     * Number of responses expected.
     */
    private int max;

    /**
     * Used to send the response on completion.
     */
    private RP rp;

    /**
     * The number of responses received.
     */
    private int c = 0;

    /**
     * The response to be sent on completion.
     */
    private Object response;

    /**
     * Create a JAResponseCounter.
     *
     * @param max Number of responses expected.
     * @param rp  Used to send the response on completion.
     */
    public JAResponseCounter(int max, RP rp) {
        this.max = max;
        this.rp = rp;
    }

    /**
     * Receives and processes a response.
     *
     * @param response The response.
     * @throws Exception Any uncaught exceptions raised when processing the response.
     */
    @Override
    public void processResponse(Object response) throws Exception {
        c += 1;
        if (c == max) rp.processResponse(null);
    }
}
