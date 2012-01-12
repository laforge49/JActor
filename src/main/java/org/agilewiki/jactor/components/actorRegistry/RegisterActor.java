package org.agilewiki.jactor.components.actorRegistry;

import org.agilewiki.jactor.components.JCActor;

/**
 * <p></p>Sent to an actor with an ActorRegistry component to register an actor
 * which has been assigned an actor name.</p>
 * <p>Only one actor can be registered for a given name.</p>
 */
final public class RegisterActor {
    /**
     * The actor to be registered.
     */
    private JCActor actor;

    /**
     * Create a RegisterActor request.
     *
     * @param actor The actor to be registered.
     */
    public RegisterActor(JCActor actor) {
        this.actor = actor;
    }

    /**
     * Returns the actor to be registered.
     *
     * @return The actor to be registered.
     */
    public JCActor getActor() {
        return actor;
    }
}
