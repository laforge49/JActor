package org.agilewiki.jactor;

/**
 * Represents a null when exiting JAIterator.iterate.
 */
final public class JANull {
    /**
     * The JANull singleton.
     */
    public final static JANull jan = new JANull();

    /**
     * Create a JANull.
     */
    private JANull() {
    }
}
