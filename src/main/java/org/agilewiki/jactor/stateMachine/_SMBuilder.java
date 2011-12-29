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
abstract public class _SMBuilder {
    final public ArrayList<_Operation> operations = new ArrayList<_Operation>();
    final public HashMap<String, Integer> labels = new HashMap<String, Integer>();

    final public void add(_Operation operation) {
        operations.add(operation);
    }

    final public void _send(Actor targetActor, Object request) {
        operations.add(new _SendVV(targetActor, request, null));
    }

    final public void _send(Actor targetActor, Object request, String resultName) {
        operations.add(new _SendVV(targetActor, request, resultName));
    }

    final public void _send(ActorFunc targetActor, ObjectFunc request) {
        operations.add(new _SendFF(targetActor, request, null));
    }

    final public void _send(ActorFunc targetActor, ObjectFunc request, String resultName) {
        operations.add(new _SendFF(targetActor, request, resultName));
    }

    final public void _send(Actor targetActor, ObjectFunc request) {
        operations.add(new _SendVF(targetActor, request, null));
    }

    final public void _send(Actor targetActor, ObjectFunc request, String resultName) {
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

    final public void _return(ObjectFunc objectFunc) {
        operations.add(new _ReturnF(objectFunc));
    }

    final public void _set(ObjectFunc objectFunc) {
        operations.add(new _SetF(objectFunc, null));
    }

    final public void _set(ObjectFunc objectFunc, String resultName) {
        operations.add(new _SetF(objectFunc, resultName));
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

    /**
     * Assign a label to the location of the next operation to be created.
     *
     * @param label The label assigned to the location of the next operation.
     */
    final public void _label(String label) {
        labels.put(label, new Integer(operations.size()));
    }

    /**
     * Create a _Goto.
     *
     * @param label The identifier of where to go to.
     */
    final public void _goto(String label) {
        new _Goto(this, label);
    }

    /**
     * Create an _IfV
     * @param condition The condition.
     * @param label The identifier of where to go to.
     */
    final public void _if(boolean condition, String label) {
        new _IfV(this, condition, label);
    }

    /**
     * Create an _IfF
     * @param condition The condition.
     * @param label The identifier of where to go to.
     */
    final public void _if(BooleanFunc condition, String label) {
        new _IfF(this, condition, label);
    }

    /**
     * Create a _Call.
     *
     * @param smb The builder of the state machine to be executed.
     */
    final public void _call(_SMBuilder smb) {
        new _Call(this, smb, null);
    }

    /**
     * Create a _Call.
     *
     * @param smb The builder of the state machine to be executed.
     * @param resultName The name of the result, or null.
     */
    final public void _call(_SMBuilder smb, String resultName) {
        new _Call(this, smb, resultName);
    }

    final public void call(ResponseProcessor rp)
            throws Exception {
        final StateMachine stateMachine = new StateMachine(this);
        stateMachine.execute(rp);
    }

    /**
     * Send a request to an actor.
     *
     * @param actor   The target actor.
     * @param request The request.
     * @param rp The response processor.
     * @throws Exception Any uncaught exceptions raised while processing the request.
     */
    abstract public void send(Actor actor, Object request, ResponseProcessor rp) throws Exception;
}
