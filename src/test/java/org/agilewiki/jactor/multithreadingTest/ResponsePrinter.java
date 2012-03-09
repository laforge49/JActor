package org.agilewiki.jactor.multithreadingTest;

import org.agilewiki.jactor.RP;
import org.agilewiki.jactor.Request;
import org.agilewiki.jactor.bind.Internals;
import org.agilewiki.jactor.bind.MethodBinding;
import org.agilewiki.jactor.components.Component;
import org.agilewiki.jactor.components.JCActor;

/**
 * Test code.
 */
public class ResponsePrinter extends Component {
    @Override
    public void bindery() throws Exception {
        thisActor.bind(PrintResponse.class.getName(), new MethodBinding<PrintResponse<Object>, Object>() {
            @Override
            public void processRequest(Internals internals,
                                       PrintResponse request,
                                       final RP rp)
                    throws Exception {
                Request wrappedRequest = request.getRequest();
                JCActor actor = request.getActor();
                wrappedRequest.send(internals, actor, new RP() {
                    @Override
                    public void processResponse(Object response) throws Exception {
                        System.out.println(response);
                        rp.processResponse(null);
                    }
                });
            }
        });
    }
}
