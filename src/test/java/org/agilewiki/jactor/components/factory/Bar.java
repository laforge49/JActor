package org.agilewiki.jactor.components.factory;

import org.agilewiki.jactor.bind.ConcurrentMethodBinding;
import org.agilewiki.jactor.bind.RequestReceiver;
import org.agilewiki.jactor.bind.VoidConcurrentMethodBinding;
import org.agilewiki.jactor.components.Component;
import org.agilewiki.jactor.components.Include;
import org.agilewiki.jactor.components.actorName.ActorName;
import org.agilewiki.jactor.components.actorName.GetActorName;
import org.agilewiki.jactor.lpc.RequestSource;

import java.util.ArrayList;

public class Bar extends Component {
    private String myName;

    @Override
    public ArrayList<Include> includes() {
        ArrayList<Include> rv = new ArrayList<Include>();
        rv.add(new Include(ActorName.class));
        return rv;
    }

    @Override
    public void bindery() throws Exception {
        super.bindery();
        thisActor.bind(Hi.class.getName(), new VoidConcurrentMethodBinding<Hi>() {
            @Override
            public void concurrentProcessRequest(RequestReceiver requestReceiver,
                                                   Hi request)
                    throws Exception {
                myName = (new GetActorName()).call(thisActor);
                System.err.println("Hello world! --" + myName);
            }
        });
    }

    @Override
    public void close() throws Exception {
        System.err.println("Bye! --" + myName);
    }
}
