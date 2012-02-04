package org.agilewiki.jactor.components.factory;

import org.agilewiki.jactor.bind.ConcurrentMethodBinding;
import org.agilewiki.jactor.bind.Internals;
import org.agilewiki.jactor.bind.RequestReceiver;
import org.agilewiki.jactor.components.Component;
import org.agilewiki.jactor.lpc.RequestSource;

public class Foo extends Component {
    @Override
    public void open(final Internals internals) throws Exception {
        super.open(internals);
        internals.bind(Hi.class.getName(), new ConcurrentMethodBinding<Hi>() {
            @Override
            public Object concurrentProcessRequest(RequestReceiver requestReceiver,
                                                   RequestSource requestSource,
                                                   Hi request)
                    throws Exception {
                System.err.println("Hello world!");
                return null;
            }
        });
    }
}
