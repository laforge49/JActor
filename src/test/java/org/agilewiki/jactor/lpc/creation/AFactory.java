package org.agilewiki.jactor.lpc.creation;

import org.agilewiki.jactor.Mailbox;
import org.agilewiki.jactor.factory.JLPCActorFactory;
import org.agilewiki.jactor.lpc.JLPCActor;

public class AFactory extends JLPCActorFactory {

    public AFactory(String actorType) {
        super(actorType);
    }

    @Override
    protected JLPCActor instantiateActor(Mailbox mailbox) throws Exception {
        return new A(mailbox);
    }
}
