package org.agilewiki.jactor.components;

import org.agilewiki.jactor.RP;
import org.agilewiki.jactor.bind.Internals;
import org.agilewiki.jactor.bind.MethodBinding;

public class C1 extends Component {
    @Override
    public void bindery()
            throws Exception {
        super.bindery();
        thisActor.bind(Hi.class.getName(), new MethodBinding<Hi, String>() {
            public void processRequest(Internals internals, Hi request, RP rp1)
                    throws Exception {
                rp1.processResponse("Hello world!");
            }
        });
    }

    @Override
    public void close() {
        System.err.println("C1 closed");
    }
}
