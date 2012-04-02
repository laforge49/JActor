package org.agilewiki.jactor.pubsub.actorName;

import org.agilewiki.jactor.lpc.TargetActor;

/**
 * Immutable actor names.
 */
public interface ActorName extends TargetActor {
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
