package org.agilewiki.jactor.components;

import org.agilewiki.jactor.ResponseProcessor;
import org.agilewiki.jactor.bind.AsyncMethodBinding;
import org.agilewiki.jactor.bind.Internals;

public class C1 extends Component {
    @Override
    public void open(final Internals internals, final ResponseProcessor rp)
            throws Exception {
        super.open(internals, new ResponseProcessor() {
            @Override
            public void process(Object response) throws Exception {
                internals.bind(Hi.class.getName(), new AsyncMethodBinding() {
                    public void processRequest(Internals internals, Object request, ResponseProcessor rp1)
                            throws Exception {
                        rp1.process("Hello world!");
                    }
                });
                rp.process(null);
            }
        });
    }

    @Override
    public void close() {
        System.err.println("C1 closed");
    }
}
