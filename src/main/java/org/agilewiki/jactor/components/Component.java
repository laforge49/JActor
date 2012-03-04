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
package org.agilewiki.jactor.components;

import org.agilewiki.jactor.bind.Internals;

import java.util.ArrayList;

/**
 * Part of a composite actor.
 */
public class Component {
    /**
     * The actor of this component.
     */
    public JCActor thisActor;

    /**
     * Returns a list of Includes for inclusion in the actor.
     *
     * @return A list of classes for inclusion in the actor.
     */
    public ArrayList<Include> includes() {
        return null;
    }

    /**
     * Bind request classes.
     *
     * @throws Exception Any exceptions thrown while binding.
     */
    public void bindery()
            throws Exception {
    }

    /**
     * Open is called when a Open initialization request is processed,
     * but before the actor is marked as active.
     *
     * @param internals The actor's internals.
     * @throws Exception Any exceptions thrown during the open.
     */
    public void open(Internals internals)
            throws Exception {
    }

    /**
     * Opened is called when a Open initialization request is processed,
     * but after the actor is marked as active.
     *
     * @param internals The actor's internals.
     * @throws Exception Any exceptions thrown during the open.
     */
    public void opened(Internals internals)
            throws Exception {
    }

    /**
     * Close any files or sockets opened by the component.
     * Components are closed in reverse dependency order, the root component being the first.
     *
     * @throws Exception All exceptions thrown will be ignored.
     */
    public void close()
            throws Exception {
    }

    /**
     * Returns true when the parent has the same component.
     *
     * @return True when the parent has the same component.
     */
    final public boolean parentHasSameComponent() {
        return thisActor.parentHasDataItem(getClass().getName());
    }
}
