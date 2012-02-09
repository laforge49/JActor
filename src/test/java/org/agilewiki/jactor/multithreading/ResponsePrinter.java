package org.agilewiki.jactor.multithreading;

import org.agilewiki.jactor.ResponseProcessor;
import org.agilewiki.jactor.bind.Internals;
import org.agilewiki.jactor.bind.MethodBinding;
import org.agilewiki.jactor.bind.RP;
import org.agilewiki.jactor.bind.Request;
import org.agilewiki.jactor.components.Component;
import org.agilewiki.jactor.components.JCActor;

public class ResponsePrinter extends Component {
    @Override
    public void bindery() throws Exception {
        thisActor.bind(PrintResponse.class.getName(), new MethodBinding<PrintResponse>() {
            @Override
            public void processRequest(Internals internals,
                                       PrintResponse request,
                                       final ResponseProcessor rp)
                    throws Exception {
                Request wrappedRequest = request.getRequest();
                JCActor actor = request.getActor();
                System.out.println("sending");
                wrappedRequest.send(internals, actor, new RP() {
                    @Override
                    public void processResponse(Object response) throws Exception {
                        System.out.println(response);
                        rp.process(null);
                    }
                });
            }
        });
    }
}
