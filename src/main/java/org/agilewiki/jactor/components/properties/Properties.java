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

import org.agilewiki.jactor.Actor;
import org.agilewiki.jactor.ResponseProcessor;
import org.agilewiki.jactor.bind.AsyncMethodBinding;
import org.agilewiki.jactor.bind.Internals;
import org.agilewiki.jactor.bind.RequestReceiver;
import org.agilewiki.jactor.bind.SyncBinding;
import org.agilewiki.jactor.components.Component;
import org.agilewiki.jactor.lpc.RequestSource;

import java.util.concurrent.ConcurrentSkipListMap;

/**
 * GetProperties first checks the component's own table of name/value pairs. If the property is not
 * found and its parent also has a Properties component, then the request is passed up to
 * the parent.
 */
public class Properties extends Component {
    /**
     * Table of registered actors.
     */
    private ConcurrentSkipListMap<String, Object> properties = new ConcurrentSkipListMap<String, Object>();

    /**
     * Initialize the component after all its includes have been processed.
     * The response must always be null;
     *
     * @param internals The JBActor's internals.
     * @throws Exception Any exceptions thrown during the open.
     */
    @Override
    public void open(final Internals internals, final ResponseProcessor rp) throws Exception {
        super.open(internals, new ResponseProcessor() {
            @Override
            public void process(Object response) throws Exception {

                internals.bind(SetProperty.class.getName(), new AsyncMethodBinding() {
                    public void processRequest(Internals internals, Object request, final ResponseProcessor rp1)
                            throws Exception {
                        SetProperty setProperty = (SetProperty) request;
                        String propertyName = setProperty.getPropertyName();
                        Object propertyValue = setProperty.getPropertyValue();
                        properties.put(propertyName, propertyValue);
                        rp1.process(null);
                    }
                });

                internals.bind(GetProperty.class.getName(), new SyncBinding() {
                    @Override
                    public void acceptRequest(RequestReceiver requestReceiver, RequestSource requestSource, Object request, ResponseProcessor rp)
                            throws Exception {
                        GetProperty getProperty = (GetProperty) request;
                        String name = getProperty.getPropertyName();
                        Object value = properties.get(name);
                        if (value == null && requestReceiver.parentHasSameComponent()) {
                            Actor parent = requestReceiver.getParent();
                            parent.acceptRequest(requestSource, request, rp);
                            return;
                        }
                        rp.process(value);
                    }
                });

                rp.process(null);
            }
        });
    }
}
