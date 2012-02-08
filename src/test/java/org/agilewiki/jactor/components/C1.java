package org.agilewiki.jactor.components;

import org.agilewiki.jactor.ResponseProcessor;
import org.agilewiki.jactor.bind.Internals;
import org.agilewiki.jactor.bind.MethodBinding;

public class C1 extends Component {
    @Override
    public void bindery()
            throws Exception {
        super.bindery();
        thisActor.bind(Hi.class.getName(), new MethodBinding<Hi>() {
            public void processRequest(Internals internals, Hi request, ResponseProcessor rp1)
                    throws Exception {
                rp1.process("Hello world!");
            }
        });
    }

    @Override
    public void close() {
        System.err.println("C1 closed");
    }
}
