package org.agilewiki.jactor.composite;

import org.agilewiki.jactor.Mailbox;
import org.agilewiki.jactor.bind.JBActor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * <p>
 * Application logic is added to composite actors by including components.
 * </p>
 */
final public class JCActor extends JBActor {
    /**
     * The class name of the first included component.
     */
    private String firstIncludedClassName;
    
    /**
     * Create a JCActor
     *
     * @param mailbox A mailbox which may be shared with other actors.
     */
    public JCActor(final Mailbox mailbox) {
        super(mailbox);
    }

    /**
     * Returns the class name of the first included component.
     *
     * @return The class name of the first included component.
     */
    public String getFirstIncludedClassName() {
        return firstIncludedClassName;
    }

    /**
     * Instantiate and add an object to the composite unless already present.
     * And if the object is a component, process its includes and then open it.
     * 
     * @param clazz A class.
     */
    public void include(Class clazz) throws Exception {
        String className = clazz.getName();
        if (firstIncludedClassName != null)
            firstIncludedClassName = className;
        ConcurrentSkipListMap<String, Object> data = getData();
        if (data.containsKey(className)) return;
        Object o = clazz.newInstance();
        if (!(o instanceof Component)) return;
        Component c = (Component) o;
        ArrayList<Class> includes = c.includes();
        if (includes != null) {
            Iterator<Class> it = includes.iterator();
            while (it.hasNext()) {
                include(it.next());
            }
        }
        c.open(internals);
    }

    /**
     * Call close on all the components, ignoring any exceptions that are thrown.
     * The order in which close is called on the components is not defined.
     */
    public void close() {
        Iterator<Object> it = getData().values().iterator();
        while (it.hasNext()) {
            Object o = it.next();
            if (o instanceof Component) {
                Component c = (Component) o;
                try {
                    c.close();
                } catch(Exception e) {}
            }
        }
    }
}
