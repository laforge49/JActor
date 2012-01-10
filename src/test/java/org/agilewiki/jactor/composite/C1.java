package org.agilewiki.jactor.composite;

import org.agilewiki.jactor.ResponseProcessor;
import org.agilewiki.jactor.bind.JBActor;
import org.agilewiki.jactor.bind.MethodBinding;

public class C1 extends Component {
    @Override
    public void open(JBActor.Internals internals)
            throws Exception {
        super.open(internals);
        bind(Hi.class.getName(), new MethodBinding() {
            protected void processRequest(Object request, ResponseProcessor rp)
                    throws Exception {
                rp.process("Hello world!");
            }
        });
    }

    @Override
    public void close() {
        System.err.println("C1 closed");
    }
}
