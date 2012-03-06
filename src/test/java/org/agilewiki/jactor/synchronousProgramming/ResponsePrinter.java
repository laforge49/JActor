package org.agilewiki.jactor.synchronousProgramming;

import org.agilewiki.jactor.bind.Internals;
import org.agilewiki.jactor.bind.SynchronousRequest;
import org.agilewiki.jactor.bind.VoidSynchronousMethodBinding;
import org.agilewiki.jactor.components.Component;
import org.agilewiki.jactor.components.JCActor;

public class ResponsePrinter extends Component {
    @Override
    public void bindery() throws Exception {
        thisActor.bind(
                PrintResponse.class.getName(),
                new VoidSynchronousMethodBinding<PrintResponse<Object>>() {
                    @Override
                    public void synchronousProcessRequest(Internals internals, PrintResponse request)
                            throws Exception {
                        SynchronousRequest wrappedRequest = request.getRequest();
                        JCActor actor = request.getActor();
                        Object response = wrappedRequest.call(internals, actor);
                        System.out.println(response);
                    }
                });
    }
}
