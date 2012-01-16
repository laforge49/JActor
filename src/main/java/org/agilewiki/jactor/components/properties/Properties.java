package org.agilewiki.jactor.components.properties;

import org.agilewiki.jactor.Actor;
import org.agilewiki.jactor.ResponseProcessor;
import org.agilewiki.jactor.bind.JBActor;
import org.agilewiki.jactor.bind.MethodBinding;
import org.agilewiki.jactor.bind.SyncBinding;
import org.agilewiki.jactor.components.Component;
import org.agilewiki.jactor.lpc.RequestSource;

import java.util.concurrent.ConcurrentSkipListMap;

/**
 * GetProperties first checks the component's own table of name/value pairs. If the property is not
 * found and its parent also has a Properties component, then the request is passed up to
 * the parent.
 */
public class Properties extends Component {
    /**
     * Table of registered actors.
     */
    private ConcurrentSkipListMap<String, Object> properties = new ConcurrentSkipListMap<String, Object>();

    /**
     * Initialize the component after all its includes have been processed.
     * The response must always be null;
     *
     * @param internals The JBActor's internals.
     * @throws Exception Any exceptions thrown during the open.
     */
    @Override
    public void open(JBActor.Internals internals, final ResponseProcessor rp) throws Exception {
        super.open(internals, new ResponseProcessor() {
            @Override
            public void process(Object response) throws Exception {

                bind(SetProperty.class.getName(), new MethodBinding() {
                    protected void processRequest(Object request, final ResponseProcessor rp1)
                            throws Exception {
                        SetProperty setProperty = (SetProperty) request;
                        String propertyName = setProperty.getPropertyName();
                        Object propertyValue = setProperty.getPropertyValue();
                        properties.put(propertyName, propertyValue);
                        rp1.process(null);
                    }
                });

                bind(GetProperty.class.getName(), new SyncBinding() {
                    @Override
                    public void acceptRequest(RequestSource requestSource, Object request, ResponseProcessor rp)
                            throws Exception {
                        GetProperty getProperty = (GetProperty) request;
                        String name = getProperty.getPropertyName();
                        Object value = properties.get(name);
                        if (value == null && parentHasSameComponent()) {
                            Actor parent = getParent();
                            parent.acceptRequest(requestSource, request, rp);
                            return;
                        }
                        rp.process(value);
                    }
                });

                rp.process(null);
            }
        });
    }
}
