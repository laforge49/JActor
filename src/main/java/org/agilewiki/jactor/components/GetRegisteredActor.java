package org.agilewiki.jactor.components;

/**
 * Sent to an actor with an ActorRegister component to fetch a registered actor by name.
 */
final public class GetRegisteredActor {
    /**
     * The name of the registered actor.
     */
    private String name;

    /**
     * Create a GetRegisteredActor request.
     *
     * @param name The name of the registered actor.
     */
    public GetRegisteredActor(String name) {
        this.name = name;
    }

    /**
     * Returns the name of the registered actor.
     *
     * @return The name of the registered actor.
     */
    public String getName() {
        return name;
    }
}
