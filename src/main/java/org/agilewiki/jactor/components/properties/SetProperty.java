package org.agilewiki.jactor.components.properties;

/**
 * This request assigns a value to a property.
 */
public class SetProperty {
    /**
     * The name of the property.
     */
    private String propertyName;

    /**
     * The value of the property.
     */
    private Object propertyValue;

    /**
     * Create a SetProperty request.
     *
     * @param propertyName The name of the property.
     * @param propertyValue  The value of the property.
     */
    public SetProperty(String propertyName, Object propertyValue) {
        this.propertyName = propertyName;
        this.propertyValue = propertyValue;
    }

    /**
     * Returns the property name/
     *
     * @return The property name.
     */
    public String getPropertyName() {
        return propertyName;
    }

    /**
     * Returns the property value.
     *
     * @return The property value, or null.
     */
    public Object getPropertyValue() {
        return propertyValue;
    }
}
