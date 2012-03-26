package org.agilewiki.jactor.components.factory;

import org.agilewiki.jactor.bind.JBInitializationRequest;

/**
 * Register a JLPCActorFactory.
 */
public class RegisterJLPCActorFactory extends JBInitializationRequest<Object> {
    /**
     * An actor type name.
     */
    private String actorType;

    /**
     * The JLPCActorFactory.
     */
    private JLPCActorFactory jlpcActorFactory;

    /**
     * Create the request.
     *
     * @param jlpcActorFactory The JLPCActorFactory.
     */
    public RegisterJLPCActorFactory(JLPCActorFactory jlpcActorFactory) {
        this.actorType = jlpcActorFactory.getActorType();
        this.jlpcActorFactory = jlpcActorFactory;
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
     * Returns the JLPCActorFactory.
     *
     * @return The JLPCActorFactory.
     */
    public JLPCActorFactory getJlpcActorFactory() {
        return jlpcActorFactory;
    }
}
