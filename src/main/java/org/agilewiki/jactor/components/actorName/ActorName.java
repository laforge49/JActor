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
package org.agilewiki.jactor.components.actorName;

import org.agilewiki.jactor.ResponseProcessor;
import org.agilewiki.jactor.bind.DataBinding;
import org.agilewiki.jactor.bind.Internals;
import org.agilewiki.jactor.bind.AsyncMethodBinding;
import org.agilewiki.jactor.components.Component;

import java.util.concurrent.ConcurrentSkipListMap;

/**
 * Implements immutable actor object names.
 * Supported request messages: SetActorName and GetActorName.
 */
public class ActorName extends Component {

    /**
     * Initialize the component after all its includes have been processed.
     * The response must always be null;
     *
     * @param internals The JBActor's internals.
     * @throws Exception Any exceptions thrown during the open.
     */
    @Override
    public void open(final Internals internals, final ResponseProcessor rp)
            throws Exception {
        super.open(internals, new ResponseProcessor() {
            @Override
            public void process(Object response) throws Exception {

                internals.bind(SetActorName.class.getName(), new AsyncMethodBinding() {
                    public void processRequest(Internals internals, Object request, ResponseProcessor rp1)
                            throws Exception {
                        ConcurrentSkipListMap<String, Object> data = internals.getData();
                        if (data.get("ActorName") != null)
                            throw new UnsupportedOperationException("Already named");
                        SetActorName setActorName = (SetActorName) request;
                        String name = setActorName.getName();
                        data.put("ActorName", name);
                        rp1.process(null);
                    }
                });

                internals.bind(GetActorName.class.getName(), new DataBinding("ActorName"));

                rp.process(null);
            }
        });
    }
}
