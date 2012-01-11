package org.agilewiki.jactor.components;

/**
 * Sent to an actor with a ActorRegistry to unregister an actor.
 */
final public class UnregisterActor {
    /**
     * The name of the actor to be unregistered.
     */
    private String name;

    /**
     * Create a UnregisterActor request.
     *
     * @param name The name of the actor to be unregistered.
     */
    public UnregisterActor(String name) {
        this.name = name;
    }

    /**
     * Returns the name of the actor to be unregistered.
     *
     * @return The name of the actor to be unregistered.
     */
    public String getName() {
        return name;
    }
}
