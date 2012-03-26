package org.agilewiki.jactor.components.factory;

import org.agilewiki.jactor.Actor;
import org.agilewiki.jactor.Mailbox;

/**
 * Creates an actor.
 */
abstract public class ActorFactory {
    /**
     * The actor type.
     */
    protected String actorType;

    /**
     * Create an ActorFactory.
     *
     * @param actorType The actor type.
     */
    protected ActorFactory(String actorType) {
        this.actorType = actorType;
    }

    /**
     * Returns the actor type.
     *
     * @return The actor type.
     */
    public String getActorType() {
        return actorType;
    }

    /**
     * Create and configure an actor.
     *
     * @param mailbox The mailbox of the new actor.
     * @param parent  The parent of the new actor.
     * @return The new actor.
     */
    abstract public Actor newActor(Mailbox mailbox, Actor parent)
            throws Exception;
}
