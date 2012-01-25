package org.agilewiki.jactor.components;

import org.agilewiki.jactor.ResponseProcessor;
import org.agilewiki.jactor.bind.JBActor;
import org.agilewiki.jactor.bind.MethodBinding;

public class C1 extends Component {
    @Override
    public void open(JBActor.Internals internals, final ResponseProcessor rp)
            throws Exception {
        super.open(internals, new ResponseProcessor() {
            @Override
            public void process(Object response) throws Exception {
                bind(Hi.class.getName(), new MethodBinding() {
                    public void processRequest(JBActor.Internals internals, Object request, ResponseProcessor rp1)
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
