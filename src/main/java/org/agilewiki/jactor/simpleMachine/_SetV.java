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
 * Assign a partial result.
 * </p>
 * <pre>
 *            SMBuilder smb = new SMBuilder();
 *            String sv = "Hello world!";
 *            smb._set(sv, "r1");
 *            sv = null;
 *            smb._set(new ObjectFunc() {
 *                public Object get(StateMachine stateMachine) {
 *                    System.out.println(stateMachine.get("r1"));
 *                    return null;
 *                }
 *            });
 *            smb.call(rp);
 *
 *            Output:
 *            Hello world!
 * </pre>
 */
public class _SetV implements _Operation {
    /**
     * The result.
     */
    private Object value;

    /**
     * The name of the result, or null.
     */
    private String resultName;

    /**
     * Create a _SetV
     *
     * @param parentSMB  The parent builder.
     * @param value      The result.
     * @param resultName The name of the result, or null.
     */
    public _SetV(_SMBuilder parentSMB, Object value, String resultName) {
        this.value = value;
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
    public void call(SimpleMachine stateMachine, RP rp) throws Exception {
        if (resultName != null) stateMachine.put(resultName, value);
        rp.processResponse(null);
    }
}
