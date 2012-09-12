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
 * Assign an (indirect) partial result.
 * </p>
 * <pre>
 *            SMBuilder smb = new SMBuilder();
 *            smb._set(new ObjectFunc() {
 *                public Object get(StateMachine stateMachine) {
 *                    System.out.println("Hello world!");
 *                    return null;
 *                }
 *            });
 *            smb._set(new ObjectFunc() {
 *                public Object get(StateMachine stateMachine) {
 *                    return "42";
 *                }
 *            }, "r1");
 *            smb._return(new ObjectFunc() {
 *                public Object get(StateMachine sm) {
 *                    return sm.get("r1");
 *                }
 *            });
 *            smb.call(rp);
 *
 *            Output:
 *            Hello world!
 *
 *            Result:
 *            42
 * </pre>
 */
final public class _SetF implements _Operation {
    /**
     * The (indirect) result.
     */
    private ObjectFunc objectFunc;

    /**
     * The name of the result, or null.
     */
    private String resultName;

    /**
     * Create a _SetF
     *
     * @param parentSMB  The parent builder.
     * @param objectFunc The (indirect) result.
     * @param resultName The name of the result, or null.
     */
    public _SetF(_SMBuilder parentSMB, ObjectFunc objectFunc, String resultName) {
        this.objectFunc = objectFunc;
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
        Object value = objectFunc.get(stateMachine);
        if (resultName != null) stateMachine.put(resultName, value);
        rp.processResponse(null);
    }
}
