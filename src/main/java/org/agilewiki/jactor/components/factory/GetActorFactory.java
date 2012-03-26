package org.agilewiki.jactor.components.factory;

import org.agilewiki.jactor.bind.JBConcurrentRequest;

/**
 * Returns the actor factory assigned to a given actor type.
 */
public class GetActorFactory extends JBConcurrentRequest<ActorFactory> {
    /**
     * An actor type name.
     */
    private String actorType;

    /**
     * Create a request.
     *
     * @param actorType An actor type name.
     */
    public GetActorFactory(String actorType) {
        this.actorType = actorType;
    }

    /**
     * Returns an actor type name.
     *
     * @return An actor type name.
     */
    public String getActorType() {
        return actorType;
    }
}
