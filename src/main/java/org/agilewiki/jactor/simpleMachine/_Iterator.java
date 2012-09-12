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

import org.agilewiki.jactor.JAIterator;
import org.agilewiki.jactor.RP;

/**
 * <p>
 * A state machine compatible extension of JAIterator.
 * </p>
 * SMBuilder smb = new SMBuilder();
 * new _Iterator(smb, "rs") {
 * int i;
 * int r = 1;
 * int max;
 * <p/>
 * protected void init(StateMachine sm) {
 * max = ((Integer) sm.request).intValue();
 * }
 * <p/>
 * protected void process(RP rp2) throws Exception {
 * if (i >= max) rp2.process(new Integer(r));
 * else {
 * i += 1;
 * r = r * i;
 * rp2.process(null);
 * }
 * }
 * };
 * smb._return(new ObjectFunc(){
 * public Object get(StateMachine sm) {
 * return sm.get("rs");
 * }
 * });
 * smb.call(5, rp);
 * <p/>
 * Response:
 * 120
 */
abstract public class _Iterator extends JAIterator implements _Operation {
    /**
     * The name of the result, or null.
     */
    private String resultName;

    /**
     * Create an _Iterator
     *
     * @param smb The state machine builder.
     */
    public _Iterator(_SMBuilder smb) {
        this(smb, null);
    }

    /**
     * Create an _Iterator
     *
     * @param smb        The state machine builder.
     * @param resultName The name of the result, or null.
     */
    public _Iterator(_SMBuilder smb, String resultName) {
        this.resultName = resultName;
        smb.add(this);
    }

    /**
     * Perform the operation.
     *
     * @param stateMachine The state machine driving the operation.
     * @param rp           The response processor.
     * @throws Exception Any uncaught exceptions raised while performing the operation.
     */
    @Override
    final public void call(final SimpleMachine stateMachine, final RP rp) throws Exception {
        init(stateMachine);
        iterate(new RP() {
            @Override
            public void processResponse(Object response) throws Exception {
                if (resultName != null) {
                    stateMachine.put(resultName, response);
                }
                rp.processResponse(null);
            }
        });
    }

    @Override
    /**
     * Iterates over the process method.
     *
     * @param responseProcessor The response processor.
     * @throws Exception Any uncaught exceptions raised by the process method.
     */
    final public void iterate(final RP responseProcessor) throws Exception {
        super.iterate(responseProcessor);
    }

    /**
     * Optional initialization, as required.
     *
     * @param stateMachine The state machine.
     */
    protected void init(SimpleMachine stateMachine) {
    }
}
