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

import org.agilewiki.jactor.RP;

/**
 * <p>
 * Instantiate and execute a subordinate state machine.
 * </p>
 * <pre>
 *            SMBuilder doubler = new SMBuilder();
 *            doubler._return(new ObjectFunc() {
 *                public Object get(StateMachine sm) {
 *                    int req = ((Integer) sm.request).intValue();
 *                    return req * 2;
 *                }
 *            });
 *
 *            SMBuilder main = new SMBuilder();
 *            main._call(doubler, new ObjectFunc() {
 *                public Object get(StateMachine sm) {
 *                    return sm.request;
 *                }
 *            }, "rsp");
 *            main._return(new ObjectFunc() {
 *                public Object get(StateMachine sm) {
 *                    return sm.get("rsp");
 *                }
 *            });
 *
 *            main.call(3, rp);
 *
 *            Response:
 *            6
 * </pre>
 */
final public class _Call implements _Operation {
    /**
     * The (indirect, optional) argument passed when the state machine was invoked.
     */
    public ObjectFunc request;

    /**
     * The name of the result, or null.
     */
    private String resultName;

    /**
     * The builder of the state machine to be executed.
     */
    private _SMBuilder smb;

    /**
     * Create a _Call.
     *
     * @param parentSMB  The parent builder.
     * @param smb        The builder of the state machine to be executed.
     * @param request    An (indirect) optional argument passed when the state machine is invoked.
     * @param resultName The name of the result, or null.
     */
    public _Call(_SMBuilder parentSMB, _SMBuilder smb, ObjectFunc request, String resultName) {
        this.smb = smb;
        this.request = request;
        this.resultName = resultName;
        parentSMB.add(this);
    }

    /**
     * Perform the operation.
     *
     * @param stateMachine The state machine driving the operation.
     * @param rp           The response processor.
     * @throws Exception Any uncaught exceptions raised while performing the operation.
     */
    @Override
    public void call(final SimpleMachine stateMachine, final RP rp)
            throws Exception {
        Object req = null;
        if (request != null) req = request.get(stateMachine);
        smb.call(req, new RP() {
            @Override
            final public void processResponse(Object response) throws Exception {
                if (resultName != null) stateMachine.put(resultName, response);
                rp.processResponse(null);
            }
        });
    }
}
