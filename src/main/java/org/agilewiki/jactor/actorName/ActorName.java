package org.agilewiki.jactor.actorName;

/**
 * Immutable actor names.
 */
public interface ActorName {
    /**
     * Returns the actor name, or null.
     *
     * @return The actor name, or null.
     */
    public String getActorName();

    /**
     * Assigns an actor name, unless already assigned.
     *
     * @param actorName The actor name.
     */
    public void setActorName(String actorName)
            throws Exception;
}
