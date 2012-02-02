package org.agilewiki.jactor.components;

import org.agilewiki.jactor.ResponseProcessor;
import org.agilewiki.jactor.bind.MethodBinding;
import org.agilewiki.jactor.bind.Internals;

public class C1 extends Component {
    @Override
    public void open(final Internals internals)
            throws Exception {
        super.open(internals);
        internals.bind(Hi.class.getName(), new MethodBinding() {
            public void processRequest(Internals internals, Object request, ResponseProcessor rp1)
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
