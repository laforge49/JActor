package org.agilewiki.jactor.factory.timing;

import org.agilewiki.jactor.factory.ActorFactory;
import org.agilewiki.jactor.lpc.JLPCActor;

public class AFactory extends ActorFactory {

    public AFactory(String actorType) {
        super(actorType);
    }

    @Override
    protected JLPCActor instantiateActor() throws Exception {
        return new A();
    }
}
