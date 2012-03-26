package org.agilewiki.jactor.components.factory;

import org.agilewiki.jactor.Mailbox;
import org.agilewiki.jactor.lpc.JLPCActor;

import java.lang.reflect.Constructor;

/**
 * Creates a JLPCActor.
 */
class _JLPCActorFactory extends JLPCActorFactory {
    /**
     * The constructor used to create the actor.
     */
    private Constructor constructor;

    /**
     * Create an ActorFactory.
     *
     * @param actorType   The actor type.
     * @param constructor The constructor used to create the actor.
     */
    public _JLPCActorFactory(String actorType, Constructor constructor) {
        super(actorType);
        this.constructor = constructor;
    }

    /**
     * Create a JLPCActor.
     *
     * @param mailbox The mailbox of the new actor.
     * @return The new actor.
     */
    protected JLPCActor instantiateActor(Mailbox mailbox)
            throws Exception {
        return (JLPCActor) constructor.newInstance(mailbox);
    }
}
