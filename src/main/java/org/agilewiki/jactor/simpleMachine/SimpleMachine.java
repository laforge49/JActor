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
package org.agilewiki.jactor.simpleMachine;

import org.agilewiki.jactor.Actor;
import org.agilewiki.jactor.JAIterator;
import org.agilewiki.jactor.JANull;
import org.agilewiki.jactor.RP;
import org.agilewiki.jactor.lpc.Request;

import java.util.HashMap;

/**
 * A state machine.
 */
public class SimpleMachine {
    /**
     * The (optional) argument passed when the state machine was invoked.
     */
    public Object request;

    /**
     * The next operation to be executed.
     */
    private int programCounter = 0;

    /**
     * A table of partial results.
     */
    final private HashMap<String, Object> results = new HashMap<String, Object>();

    /**
     * The state machine builder which defines the operations of this state machine.
     */
    private _SMBuilder smBuilder;

    /**
     * Create a StateMachine.
     *
     * @param smBuilder The state machine builder which defines the operations of this state machine.
     */
    public SimpleMachine(_SMBuilder smBuilder) {
        this.smBuilder = smBuilder;
    }

    /**
     * Executes the state machine.
     *
     * @param request An optional argument passed when the state machine is invoked.
     * @param rp      The response processor.
     * @throws Exception Any exceptions raised while executing the state machine.
     */
    public void execute(Object request, RP rp) throws Exception {
        this.request = request;
        (new JAIterator() {
            @Override
            protected void process(final RP rp1) throws Exception {
                if (programCounter >= smBuilder.operationsSize()) rp1.processResponse(JANull.jan);
                else {
                    final _Operation o = smBuilder.getOperation(programCounter);
                    programCounter += 1;
                    o.call(SimpleMachine.this, rp1);
                }
            }
        }).iterate(rp);
    }

    /**
     * Returns a partial result.
     *
     * @param resultName The name of the partial result.
     * @return A partial result.
     */
    final public Object get(Object resultName) {
        return results.get(resultName);
    }

    /**
     * Save a parial result.
     *
     * @param resultName The name of the result.
     * @param result     The result.
     */
    final public void put(String resultName, Object result) {
        results.put(resultName, result);
    }

    /**
     * Send a request to an actor.
     *
     * @param actor   The target actor.
     * @param request The request.
     * @param rp      The response processor.
     * @throws Exception Any uncaught exceptions raised while processing the request.
     */
    public void send(Actor actor, Request request, RP rp)
            throws Exception {
        smBuilder.send(actor, request, rp);
    }

    /**
     * Update the program counter with the index assigned to a label.
     *
     * @param label A lable assigned to an index into the operations.
     * @throws IllegalArgumentException Thrown if no value was assigned to the label.
     */
    public void go(String label) throws IllegalArgumentException {
        Integer loc = smBuilder.resolve(label);
        if (loc == null) throw new IllegalArgumentException("unknown label: " + label);
        programCounter = loc.intValue();
    }
}
