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
package org.agilewiki.jactor.stateMachine;

import org.agilewiki.jactor.Actor;
import org.agilewiki.jactor.JAIterator;
import org.agilewiki.jactor.ResponseProcessor;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Creates and runs a state machine.
 */
abstract public class _StateMachine {
    final public ArrayList<_Operation> operations = new ArrayList<_Operation>();
    final public HashMap<String, Integer> labels = new HashMap<String, Integer>();

    final public void _send(Actor targetActor, Object request) {
        operations.add(new _SendVV(targetActor, request, null));
    }

    final public void _send(Actor targetActor, Object request, String resultName) {
        operations.add(new _SendVV(targetActor, request, resultName));
    }

    final public void _send(ActorFunc targetActor, Func request) {
        operations.add(new _SendFF(targetActor, request, null));
    }

    final public void _send(ActorFunc targetActor, Func request, String resultName) {
        operations.add(new _SendFF(targetActor, request, resultName));
    }

    final public void _send(Actor targetActor, Func request) {
        operations.add(new _SendVF(targetActor, request, null));
    }

    final public void _send(Actor targetActor, Func request, String resultName) {
        operations.add(new _SendVF(targetActor, request, resultName));
    }

    final public void _send(ActorFunc targetActor, Object request) {
        operations.add(new _SendFV(targetActor, request, null));
    }

    final public void _send(ActorFunc targetActor, Object request, String resultName) {
        operations.add(new _SendFV(targetActor, request, resultName));
    }

    final public void _return(Object value) {
        operations.add(new _ReturnV(value));
    }

    final public void _return(Func func) {
        operations.add(new _ReturnF(func));
    }

    final public void _set(Func func) {
        operations.add(new _SetF(func, null));
    }

    final public void _set(Func func, String resultName) {
        operations.add(new _SetF(func, resultName));
    }

    final public void _set(Object value) {
        operations.add(new _SetV(value, null));
    }

    final public void _set(Object value, String resultName) {
        operations.add(new _SetV(value, resultName));
    }

    final public void _iterator(JAIterator iterator) {
        operations.add(new _Iterator(iterator, null));
    }

    final public void _iterator(JAIterator iterator, String resultName) {
        operations.add(new _Iterator(iterator, resultName));
    }

    final public void _label(String label) {
        labels.put(label, new Integer(operations.size()));
    }

    final public void _go(String label) {
        operations.add(new _Go(label));
    }

    final public void _if(boolean condition, String label) {
        operations.add(new _IfV(condition, label));
    }

    final public void _if(BooleanFunc condition, String label) {
        operations.add(new _IfF(condition, label));
    }

    final public void _call(_StateMachine comp) {
        operations.add(new _Call(comp, null));
    }

    final public void _call(_StateMachine comp, String resultName) {
        operations.add(new _Call(comp, resultName));
    }

    final public void call(ResponseProcessor rp)
            throws Exception {
        final StateMachine stateMachine = new StateMachine(this);
        setState(stateMachine);
        stateMachine.execute(rp);
    }

    final public Object get(String resultName) {
        return getState().results.get(resultName);
    }

    abstract protected StateMachine getState();

    abstract protected void setState(StateMachine stateMachine);

    /**
     * Send a request to an actor.
     *
     * @param actor   The target actor.
     * @param request The request.
     * @throws Exception Any uncaught exceptions raised while processing the request.
     */
    abstract public void send(Actor actor, Object request, ResponseProcessor rp) throws Exception;
}
