package org.agilewiki.jactor.components.factory;

import org.agilewiki.jactor.Actor;
import org.agilewiki.jactor.Mailbox;
import org.agilewiki.jactor.components.Include;
import org.agilewiki.jactor.components.JCActor;

/**
 * Creates a JCActor.
 */
final public class JCActorFactory extends ActorFactory {
    /**
     * The class of the root component.
     */
    private Class componentClass;

    /**
     * Create an ActorFactory.
     *
     * @param actorType      The actor type.
     * @param componentClass The class of the root component.
     */
    public JCActorFactory(String actorType, Class componentClass) {
        super(actorType);
        this.componentClass = componentClass;
    }

    /**
     * Create and configure an actor.
     *
     * @param mailbox The mailbox of the new actor.
     * @param parent  The parent of the new actor.
     * @return The new actor.
     */
    @Override
    public JCActor newActor(Mailbox mailbox, Actor parent) throws Exception {
        Include include = new Include(componentClass);
        JCActor actor = new JCActor(mailbox);
        actor.setActorType(actorType);
        actor.setParent(parent);
        include.call(actor);
        return actor;
    }
}
