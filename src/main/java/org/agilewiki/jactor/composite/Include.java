package org.agilewiki.jactor.composite;

/**
 * Instantiate and add an object to the composite unless already present.
 * And if the object is a component, process its includes and then open it.
 * The response is always null.
 */
final public class Include {
    private Class clazz;

    /**
     * Create an Include request.
     *
     * @param clazz A Class.
     */
    public Include(Class clazz) {
        this.clazz = clazz;
    }

    /**
     * Returns the class.
     *
     * @return The class.
     */
    public Class getClazz() {
        return clazz;
    }
}
