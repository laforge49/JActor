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

import org.agilewiki.jactor.Mailbox;
import org.agilewiki.jactor.RP;
import org.agilewiki.jactor.lpc.JLPCActor;

import java.util.concurrent.ConcurrentSkipListMap;

/**
 * GetProperties first checks the component's own table of name/value pairs. If the property is not
 * found and its parent also has a Properties component, then the request is passed up to
 * the parent.
 */
public class JAProperties<RESPONSE_TYPE>
        extends JLPCActor
        implements _Properties<RESPONSE_TYPE> {
    /**
     * Table of registered actors.
     */
    private ConcurrentSkipListMap<String, RESPONSE_TYPE> properties =
            new ConcurrentSkipListMap<String, RESPONSE_TYPE>();

    /**
     * Create a LiteActor
     *
     * @param mailbox A mailbox which may be shared with other actors.
     */
    public JAProperties(Mailbox mailbox) {
        super(mailbox);
    }

    /**
     * Get the value of a property.
     *
     * @param getProperty The request.
     * @return The value of the property, or null.
     */
    @Override
    public RESPONSE_TYPE getProperty(GetProperty<RESPONSE_TYPE> getProperty)
            throws Exception {
        String propertyName = getProperty.getPropertyName();
        if (properties.containsKey(propertyName))
            return properties.get(propertyName);
        _Properties<RESPONSE_TYPE> p = getProperty.getTargetActor(getParent());
        if (p == null)
            return null;
        return getProperty.call(p);
    }

    /**
     * Assign a value to a property.
     *
     * @param propertyName  The name of the property.
     * @param propertyValue The value to be assigned.
     */
    @Override
    public void setProperty(String propertyName, RESPONSE_TYPE propertyValue) {
        properties.put(propertyName, propertyValue);
    }

    /**
     * The application method for processing requests sent to the actor.
     *
     * @param request A request.
     * @param rp      The response processor.
     * @throws Exception Any uncaught exceptions raised while processing the request.
     */
    @Override
    protected void processRequest(Object request, RP rp) throws Exception {
        Class curReq = request.getClass();

        if (curReq == GetProperty.class) {
            rp.processResponse(getProperty((GetProperty<RESPONSE_TYPE>) request));
            return;
        }

        if (curReq == SetProperty.class) {
            SetProperty<RESPONSE_TYPE> req = (SetProperty<RESPONSE_TYPE>) request;
            setProperty(req.getPropertyName(), req.getPropertyValue());
            rp.processResponse(null);
            return;
        }

        throw new UnsupportedOperationException(request.getClass().getName());
    }
}
