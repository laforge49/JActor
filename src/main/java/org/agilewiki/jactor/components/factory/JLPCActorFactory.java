package org.agilewiki.jactor.components.factory;

import org.agilewiki.jactor.Actor;
import org.agilewiki.jactor.Mailbox;

import java.lang.reflect.Constructor;

/**
 * Creates a JLPCActor.
 */
public class JLPCActorFactory extends ActorFactory {
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
    public JLPCActorFactory(String actorType, Constructor constructor) {
        super(actorType);
        this.constructor = constructor;
    }

    /**
     * Create an actor.
     *
     * @param mailbox The mailbox of the new actor.
     * @param parent  The parent of the new actor.
     * @return The new actor.
     */
    @Override
    public Actor newActor(Mailbox mailbox, Actor parent) throws Exception {
        Actor a = (Actor) constructor.newInstance(mailbox);
        a.setActorType(actorType);
        a.setParent(parent);
        return a;
    }
}
