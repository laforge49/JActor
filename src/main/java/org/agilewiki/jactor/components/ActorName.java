package org.agilewiki.jactor.components;

import org.agilewiki.jactor.ResponseProcessor;
import org.agilewiki.jactor.bind.DataBinding;
import org.agilewiki.jactor.bind.JBActor;
import org.agilewiki.jactor.bind.MethodBinding;
import org.agilewiki.jactor.composite.Component;

import java.util.concurrent.ConcurrentSkipListMap;

/**
 * Implements immutable actor object names.
 * Supported request messages: SetActorName and GetActorName.
 */
public class ActorName extends Component {
    @Override
    public void open(JBActor.Internals internals, final ResponseProcessor rp)
            throws Exception {
        super.open(internals, new ResponseProcessor() {
            @Override
            public void process(Object response) throws Exception {

                bind(SetActorName.class.getName(), new MethodBinding() {
                    protected void processRequest(Object request, ResponseProcessor rp1)
                            throws Exception {
                        ConcurrentSkipListMap<String, Object> data = getData();
                        if (data.get("ActorName") != null)
                            throw new UnsupportedOperationException("Already named");
                        SetActorName setActorName = (SetActorName) request;
                        String name = setActorName.getName();
                        data.put("ActorName", name);
                        rp1.process(null);
                    }
                });
                
                bind(GetActorName.class.getName(), new DataBinding("ActorName"));
                
                rp.process(null);
            }
        });
    }
}
