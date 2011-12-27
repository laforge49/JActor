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
package org.agilewiki.jactor.compose;

import org.agilewiki.jactor.Actor;
import org.agilewiki.jactor.JAIterator;
import org.agilewiki.jactor.JANull;
import org.agilewiki.jactor.ResponseProcessor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Composes a series of calls to send.
 */
abstract public class _Compose {
    final private ArrayList<Operation> operations = new ArrayList<Operation>();
    final public HashMap<String, Object> results = new HashMap<String, Object>();

    final public Object get(String resultName) {
        return results.get(resultName);
    }

    final public void _send(Actor targetActor, Object request) {
        operations.add(new SendVV(targetActor, request, null));
    }

    final public void _send(Actor targetActor, Object request, String resultName) {
        operations.add(new SendVV(targetActor, request, resultName));
    }

    final public void _send(ActorFunc targetActor, Func request) {
        operations.add(new SendFF(targetActor, request, null));
    }

    final public void _send(ActorFunc targetActor, Func request, String resultName) {
        operations.add(new SendFF(targetActor, request, resultName));
    }

    final public void _send(Actor targetActor, Func request) {
        operations.add(new SendVF(targetActor, request, null));
    }

    final public void _send(Actor targetActor, Func request, String resultName) {
        operations.add(new SendVF(targetActor, request, resultName));
    }

    final public void _send(ActorFunc targetActor, Object request) {
        operations.add(new SendFV(targetActor, request, null));
    }

    final public void _send(ActorFunc targetActor, Object request, String resultName) {
        operations.add(new SendFV(targetActor, request, resultName));
    }

    final public void _return(Object value) {
        operations.add(new ReturnV(value));
    }

    final public void _return(Func func) {
        operations.add(new ReturnF(func));
    }

    final public void call(ResponseProcessor rp) throws Exception {
        (new JAIterator(rp) {
            Iterator<Operation> it = operations.iterator();

            @Override
            protected void process(final ResponseProcessor rp1) throws Exception {
                if (!it.hasNext()) rp1.process(new JANull());
                else {
                    final Operation o = it.next();
                    o.call(_Compose.this, rp1);
                }
            }
        }).iterate();
    }

    /**
     * Send a request to an actor.
     *
     * @param actor   The target actor.
     * @param request The request.
     * @throws Exception Any uncaught exceptions raised while processing the request.
     */
    abstract public void send(Actor actor, Object request, ResponseProcessor rp) throws Exception;
}
