package org.agilewiki.jactor.components.factory;

import org.agilewiki.jactor.ResponseProcessor;
import org.agilewiki.jactor.bind.Internals;
import org.agilewiki.jactor.bind.MethodBinding;
import org.agilewiki.jactor.components.Component;

public class Foo extends Component {
    @Override
    public void open(final Internals internals, final ResponseProcessor rp) throws Exception {
        super.open(internals, new ResponseProcessor() {
            @Override
            public void process(Object response) throws Exception {
                internals.bind(Hi.class.getName(), new MethodBinding() {
                    @Override
                    public void processRequest(Internals internals, Object request, ResponseProcessor rp1)
                            throws Exception {
                        System.err.println("Hello world!");
                        rp1.process(null);
                    }
                });
                rp.process(null);
            }
        });
    }
}
