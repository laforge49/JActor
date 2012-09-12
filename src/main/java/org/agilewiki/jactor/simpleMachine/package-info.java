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

/**
 * <p>
 * You can think of a State Machine as a kind of computer. The three main parts of a computer are the program, the
 * program counter and the memory.
 * </p><p>
 * The program is an array of operations which are executed by the state machine. The program counter is an index which
 * identifies the next operation to be executed. The execution cycle of a state machine is a loop with 3 steps:
 * </p><ol>
 * <li>Fetch the operation identified by the program counter.</li>
 * <li>Increment the program counter by 1. And</li>
 * <li>Execute the operation that was fetched.</li>
 * <p>
 * When you reach the end of the array, the state machine exits.
 * </p><p>
 * An operation can change the value of the program counter. This is how a goto works. But to make things just a little
 * friendlier, we add a table called labels. As we create the program, we can capture the index of the next location in
 * the program array and save it with a string for a key in the labels table. This way a goto operation can be given
 * the name of a location, which it looks up in the labels table, rather than an index into the array of operations.
 * </p><p>
 * The memory of the state machine is another table called results. This table also uses strings as keys. And an
 * operation can easily access and update the results table.
 * </p><p>
 * Why use a state machine? Because it is simpler to program a state machine to execute a series of operations than it
 * is to code it directly, when each operation has a callback which is invoked when the operation completes. The state
 * machine handles all the callbacks, allowing you to focus on the operations to be performed.
 * </p>
 * <p>
 * The nested class JLPCActor.SMBuilder is a concrete implementation of _SMBuilder
 * and is used to define, instantiate and execute state machines.
 * </p>
 * <pre>
 *    class ReturnF1 extends JLPCActor {
 *
 *        ReturnF1(Mailbox mailbox) {
 *            super(mailbox);
 *        }
 *
 *        public void processRequest(Object unwrappedRequest, RP rp)
 *                throws Exception {
 *            SMBuilder smb = new SMBuilder();
 *            smb._return(new ObjectFunc() {
 *                public Object get(StateMachine sm) {
 *                    return "Hello world!";
 *                }
 *            });
 *            smb.call(rp);
 *        }
 *    }
 *
 *            Result:
 *            Hello world!
 * </pre>
 */
package org.agilewiki.jactor.simpleMachine;