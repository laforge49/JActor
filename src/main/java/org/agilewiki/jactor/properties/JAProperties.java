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
package org.agilewiki.jactor.properties;

import org.agilewiki.jactor.Actor;
import org.agilewiki.jactor.lpc.JLPCActor;

import java.util.concurrent.ConcurrentSkipListMap;

/**
 * GetProperties first checks the component's own table of name/value pairs. If the property is not
 * found and its parent also has a Properties component, then the request is passed up to
 * the parent.
 */
public class JAProperties
        extends JLPCActor
        implements Properties {

    public static Object getProperty(Actor targetActor, String propertyName)
            throws Exception {
        if (targetActor != null)
            targetActor = targetActor.getMatch(Properties.class);
        if (targetActor == null)
            throw new UnsupportedOperationException("getProperty");
        return ((Properties) targetActor).getProperty(propertyName);
    }

    public static void setProperty(Actor targetActor, String propertyName, Object propertyValue)
            throws Exception {
        if (targetActor != null)
            targetActor = targetActor.getMatch(Properties.class);
        if (targetActor == null)
            throw new UnsupportedOperationException("getProperty");
        ((Properties) targetActor).setProperty(propertyName, propertyValue);
    }

    /**
     * Table of registered actors.
     */
    private ConcurrentSkipListMap<String, Object> properties =
            new ConcurrentSkipListMap<String, Object>();

    @Override
    public Object getProperty(String propertyName)
            throws Exception {
        if (properties.containsKey(propertyName))
            return properties.get(propertyName);
        Actor targetActor = getParent();
        if (targetActor != null)
            targetActor = targetActor.getMatch(Properties.class);
        if (targetActor == null)
            return null;
        return ((Properties) targetActor).getProperty(propertyName);
    }

    /**
     * Assign a value to a property.
     *
     * @param propertyName  The name of the property.
     * @param propertyValue The value to be assigned.
     */
    @Override
    public void setProperty(String propertyName, Object propertyValue) {
        properties.put(propertyName, propertyValue);
    }
}
