package org.agilewiki.jactor.synchronousProgramming;

import org.agilewiki.jactor.bind.Internals;
import org.agilewiki.jactor.bind.SynchronousMethodBinding;
import org.agilewiki.jactor.components.Component;

public class Greeter extends Component {
    @Override
    public void bindery() throws Exception {
        thisActor.bind(Hi.class.getName(), new SynchronousMethodBinding<Hi, String>() {
            @Override
            public String synchronousProcessRequest(Internals internals, Hi request) throws Exception {
                System.out.println("got it");
                return "Hello world!";
            }
        });
    }
}
