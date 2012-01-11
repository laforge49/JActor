package org.agilewiki.jactor.composite;

import org.agilewiki.jactor.JAIterator;
import org.agilewiki.jactor.JANull;
import org.agilewiki.jactor.Mailbox;
import org.agilewiki.jactor.ResponseProcessor;
import org.agilewiki.jactor.bind.JBActor;
import org.agilewiki.jactor.bind.MethodBinding;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * <p>
 * Application logic is added to a JCActor by including components rather than by subclassing.
 * </p>
 * <p>
 * JCActors are fully interoperable with JLPCActors and JBActors.
 * </p>
 */
final public class JCActor extends JBActor {
    /**
     * The class name of the first included component.
     */
    private String firstIncludedClassName;

    /**
     * Create a JCActor.
     *
     * @param mailbox A mailbox which may be shared with other actors.
     */
    public JCActor(final Mailbox mailbox) {
        super(mailbox);

        bind(Include.class.getName(), new MethodBinding() {
            @Override
            protected void processRequest(Object request, ResponseProcessor rp) throws Exception {
                processInclude(request, rp);
            }
        });
    }

    private void processInclude(Object request, ResponseProcessor rp) throws Exception {
        Include include = (Include) request;
        Class clazz = include.getClazz();
        final String className = clazz.getName();
        if (firstIncludedClassName != null)
            firstIncludedClassName = className;
        ConcurrentSkipListMap<String, Object> data = getData();
        if (data.containsKey(className)) {
            rp.process(null);
            return;
        }
        Object o = clazz.newInstance();
        data.put(className, o);
        if (!(o instanceof Component)) {
            rp.process(null);
            return;
        }
        final Component c = (Component) o;
        ArrayList<Include> includes = c.includes();
        if (includes == null) {
            c.open(internals, rp);
            return;
        }
        final Iterator<Include> it = includes.iterator();
        (new JAIterator() {
            @Override
            protected void process(final ResponseProcessor rp1) throws Exception {
                if (it.hasNext()) {
                    processInclude(it.next(), rp1);
                } else {
                    c.open(internals, new ResponseProcessor() {
                        @Override
                        public void process(Object response) throws Exception {
                            rp1.process(new JANull());
                        }
                    });
                }
            }
        }).iterate(rp);
    }

    /**
     * Returns the class name of the first include.
     *
     * @return The class name of the first include.
     */
    public String getFirstIncludedClassName() {
        return firstIncludedClassName;
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
                } catch (Exception e) {
                }
            }
        }
    }
}
