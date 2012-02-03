package org.agilewiki.jactor.components.factory;

import org.agilewiki.jactor.ResponseProcessor;
import org.agilewiki.jactor.bind.Internals;
import org.agilewiki.jactor.bind.MethodBinding;
import org.agilewiki.jactor.components.Component;

public class Foo extends Component {
    @Override
    public void open(final Internals internals) throws Exception {
        super.open(internals);
        internals.bind(Hi.class.getName(), new MethodBinding() {
            @Override
            public void processRequest(Internals internals, Object request, ResponseProcessor rp1)
                    throws Exception {
                System.err.println("Hello world!");
                rp1.process(null);
            }
        });
    }
}
