package org.agilewiki.jactor.components;

import org.agilewiki.jactor.Actor;

/**
 * Sent to an actor with an ActorRegister component to register an actor
 * which has been assigned an actor name.
 */
final public class RegisterActor {
    /**
     * The actor to be registered.
     */
    private Actor actor;

    /**
     * Create a RegisterActor request.
     *
     * @param actor The actor to be registered.
     */
    public RegisterActor(Actor actor) {
        this.actor = actor;
    }

    /**
     * Returns the actor to be registered.
     *
     * @return The actor to be registered.
     */
    public Actor getActor() {
        return actor;
    }
}
