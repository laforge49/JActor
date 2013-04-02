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

import java.util.List;

/**
 * Counts the number of responses received
 * and responds with the count of requests sent when the iteration is finished.
 */
public class JAResponseCounter3 extends RP {
    /**
     * The number of requests sent.
     */
    public int sent;

    /**
     * The number of responses received.
     */
    private int received;

    /**
     * True when all responses have been received.
     */
    private boolean complete;

    /**
     * The mechanism for responding when finished.
     */
    private RP xrp;

    private List<JAResponseCounter3> pool;

    public JAResponseCounter3(List<JAResponseCounter3> pool) {
        this.pool = pool;
        pool.add(this);
    }

    /**
     * Initialize.
     *
     * @param xrp The external RP
     */
    public void setup(RP xrp) {
        sent = 0;
        received = 0;
        complete = false;
        this.xrp = xrp;
    }

    /**
     * Signals that all requests have been sent.
     *
     * @throws Exception Any excpetions raised while processing the external response.
     */
    public void finished() throws Exception {
        if (received == sent) {
            pool.add(this);
            xrp.processResponse(new Integer(sent));
            xrp = null;
        } else {
            complete = true;
        }
    }

    /**
     * Receives and processes a response.
     *
     * @param response The response.
     * @throws Exception Any uncaught exceptions raised when processing the response.
     */
    @Override
    public void processResponse(Object response) throws Exception {
        received += 1;
        if (complete && received == sent) {
            pool.add(this);
            xrp.processResponse(new Integer(sent));
            xrp = null;
        }
    }
}
