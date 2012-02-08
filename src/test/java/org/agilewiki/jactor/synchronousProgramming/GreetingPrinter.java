package org.agilewiki.jactor.synchronousProgramming;

import org.agilewiki.jactor.bind.Internals;
import org.agilewiki.jactor.bind.VoidSynchronousMethodBinding;
import org.agilewiki.jactor.components.Component;

public class GreetingPrinter extends Component {
    public Hi hi = new Hi();

    @Override
    public void bindery() throws Exception {
        thisActor.bind(PrintGreeting.class.getName(), new VoidSynchronousMethodBinding<PrintGreeting>() {
            @Override
            public void synchronousProcessRequest(Internals internals, PrintGreeting request) throws Exception {
                String greeting = hi.call(internals, thisActor);
                System.out.println(greeting);
            }
        });
    }
}
