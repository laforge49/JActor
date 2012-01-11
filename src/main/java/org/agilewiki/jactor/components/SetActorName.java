package org.agilewiki.jactor.components;

/**
 * Assigns an immutable name to an actor when sent to an actor with an ActorName component.
 */
final public class SetActorName {
    /**
     * The name to be assigned to the actor.
     */
    private String name;

    /**
     * Create a SetActorName request.
     *
     * @param name The name to be assigned to the actor.
     */
    public SetActorName(String name) {
        this.name = name;
    }

    /**
     * Returns the name of an actor.
     * 
     * @return The name to be assigned to the actor.
     */
    public String getName() {
        return name;
    }
}
