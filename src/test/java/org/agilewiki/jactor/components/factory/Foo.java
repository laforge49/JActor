package org.agilewiki.jactor.components.factory;

import org.agilewiki.jactor.bind.RequestReceiver;
import org.agilewiki.jactor.bind.VoidConcurrentMethodBinding;
import org.agilewiki.jactor.components.Component;

public class Foo extends Component {
    @Override
    public void bindery() throws Exception {
        super.bindery();
        thisActor.bind(Hi.class.getName(), new VoidConcurrentMethodBinding<Hi>() {
            @Override
            public void concurrentProcessRequest(RequestReceiver requestReceiver,
                                                 Hi request)
                    throws Exception {
                System.out.println("Hello world!");
            }
        });
    }
}
