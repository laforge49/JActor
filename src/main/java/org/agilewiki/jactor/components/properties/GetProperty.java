package org.agilewiki.jactor.components.properties;

/**
 * The result of this request is the property value, or null.
 */
public class GetProperty {
    /**
     * The name of the property.
     */
    private String propertyName;

    /**
     * Create a GetProperty request.
     *
     * @param propertyName The name of the property.
     */
    public GetProperty(String propertyName) {
        this.propertyName = propertyName;
    }

    /**
     * Returns the property name.
     *
     * @return The property name.
     */
    public String getPropertyName() {
        return propertyName;
    }
}
