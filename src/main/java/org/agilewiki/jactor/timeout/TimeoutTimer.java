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
package org.agilewiki.jactor.timeout;

import org.agilewiki.jactor.Actor;
import org.agilewiki.jactor.RP;
import org.agilewiki.jactor.lpc.JLPCActor;
import org.agilewiki.jactor.lpc.Request;

import java.util.Timer;
import java.util.TimerTask;

public class TimeoutTimer extends JLPCActor {
    protected Timer timer = new Timer();

    public void close() {
        timer.cancel();
    }

    void timeout(RP rrp) throws Exception {
        rrp.processResponse(null);
    }

    void delay(long ms, RP rp) {
        timer.schedule(new TimeoutTask(this, rp), ms);
    }
}

class TimeoutTask extends TimerTask {
    TimeoutTimer timeoutTimer;
    RP rrp;

    TimeoutTask(TimeoutTimer timeoutTimer, RP rrp) {
        this.timeoutTimer = timeoutTimer;
        this.rrp = rrp;
    }

    @Override
    public void run() {
        try {
            (new Timeout(rrp)).sendEvent(timeoutTimer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

class Timeout extends Request<Object, TimeoutTimer> {
    private RP rrp;

    Timeout(RP rrp) {
        this.rrp = rrp;
    }

    @Override
    public boolean isTargetType(Actor targetActor) {
        return targetActor instanceof TimeoutTimer;
    }

    @Override
    public void processRequest(JLPCActor targetActor, RP rp) throws Exception {
        ((TimeoutTimer) targetActor).timeout(rrp);
        rp.processResponse(null);
    }
}