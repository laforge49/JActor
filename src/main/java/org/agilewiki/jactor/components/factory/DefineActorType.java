package org.agilewiki.jactor.components.factory;

/**
 * DefineActorType is a request to regesister a component that can be used
 * to create and configure new types of actors.
 */
public class DefineActorType {
    /**
     * An actor type name.
     */
    private String actorType;

    /**
     * The class used to configure an actor.
     */
    private Class rootComponentClass;

    /**
     * Create a DefineActorType request.
     *
     * @param actorType An actor type name.
     * @param rootComponentClass The class used to configure an actor.
     */
    public DefineActorType(String actorType, Class rootComponentClass) {
        this.actorType = actorType;
        this.rootComponentClass = rootComponentClass;
    }

    /**
     * Returns an actor type name.
     *
     * @return An actor type name.
     */
    public String getActorType() {
        return actorType;
    }

    /**
     * Returns the class used to configure an actor.
     *
     * @return The class used to configure an actor.
     */
    public Class getRootComponentClass() {
        return rootComponentClass;
    }
}
