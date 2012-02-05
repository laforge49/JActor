package org.agilewiki.jactor.components.factory;

import org.agilewiki.jactor.bind.ConcurrentMethodBinding;
import org.agilewiki.jactor.bind.RequestReceiver;
import org.agilewiki.jactor.components.Component;
import org.agilewiki.jactor.lpc.RequestSource;

public class Foo extends Component {
    @Override
    public void bindery() throws Exception {
        super.bindery();
        thisActor.bind(Hi.class.getName(), new ConcurrentMethodBinding<Hi, Object>() {
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
