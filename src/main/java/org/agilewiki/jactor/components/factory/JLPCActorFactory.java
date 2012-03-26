package org.agilewiki.jactor.components.factory;

import org.agilewiki.jactor.Actor;
import org.agilewiki.jactor.Mailbox;
import org.agilewiki.jactor.lpc.JLPCActor;

/**
 * Creates a JLPCActor.
 */
abstract public class JLPCActorFactory extends ActorFactory {
    /**
     * Create an ActorFactory.
     *
     * @param actorType The actor type.
     */
    public JLPCActorFactory(String actorType) {
        super(actorType);
    }

    /**
     * Create a JLPCActor.
     *
     * @param mailbox The mailbox of the new actor.
     * @return The new actor.
     */
    abstract protected JLPCActor instantiateActor(Mailbox mailbox)
            throws Exception;

    /**
     * Create and configure an actor.
     *
     * @param mailbox The mailbox of the new actor.
     * @param parent  The parent of the new actor.
     * @return The new actor.
     */
    @Override
    final public JLPCActor newActor(Mailbox mailbox, Actor parent) throws Exception {
        JLPCActor a = instantiateActor(mailbox);
        a.setActorType(actorType);
        a.setParent(parent);
        return a;
    }
}
