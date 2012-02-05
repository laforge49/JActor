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
package org.agilewiki.jactor.components.properties;

import org.agilewiki.jactor.bind.ConcurrentRequest;

/**
 * This request assigns a value to a property.
 */
public class SetProperty extends ConcurrentRequest {
    /**
     * The name of the property.
     */
    private String propertyName;

    /**
     * The value of the property.
     */
    private Object propertyValue;

    /**
     * Create a SetProperty request.
     *
     * @param propertyName  The name of the property.
     * @param propertyValue The value of the property.
     */
    public SetProperty(String propertyName, Object propertyValue) {
        this.propertyName = propertyName;
        this.propertyValue = propertyValue;
    }

    /**
     * Returns the property name/
     *
     * @return The property name.
     */
    public String getPropertyName() {
        return propertyName;
    }

    /**
     * Returns the property value.
     *
     * @return The property value, or null.
     */
    public Object getPropertyValue() {
        return propertyValue;
    }
}
