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
package org.agilewiki.jactor.lpc;

import org.agilewiki.jactor.apc.APCMailbox;
import org.agilewiki.jactor.apc.JAPCMessage;
import org.agilewiki.jactor.bufferedEvents.BufferedEventsDestination;

/**
 * Serves as the asynchronous transport for APCMessages.
 */
public interface LPCMailbox extends APCMailbox {

    /**
     * Returns the controlling mailbox, or null.
     */
    public LPCMailbox getControllingMailbox();

    /**
     * Gains control over the mailbox.
     *
     * @param srcControllingMailbox The mailbox gaining control.
     * @return True when control was acquired.
     */
    public boolean acquireControl(LPCMailbox srcControllingMailbox);

    /**
     * Relinquish control over the mailbox.
     */
    public void relinquishControl();

    /**
     * Returns true when all requests are to be processed asynchronously.
     *
     * @return True when all requests are to be processed asynchronously.
     */
    public boolean isAsync();

    /**
     * Dispatch any enqueued requests, if possible.
     *
     * @param controllingMailbox The mailbox that was just in control.
     */
    public void dispatchRemaining(LPCMailbox controllingMailbox);
}
