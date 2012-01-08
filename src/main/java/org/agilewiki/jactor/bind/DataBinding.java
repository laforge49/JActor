package org.agilewiki.jactor.bind;

import org.agilewiki.jactor.ResponseProcessor;
import org.agilewiki.jactor.lpc.RequestSource;

/**
 * Binds a request class to a concurrent data item.
 */
public class DataBinding extends Binding {
    /**
     * The name of a concurrent data item.
     */
    private String name;

    /**
     * Create a DataBinding.
     *
     * @param name The name of a concurrent data item.
     */
    public DataBinding(String name) {
        this.name = name;
    }

    /**
     * Process an incoming request.
     *
     * @param requestSource The originator of the request.
     * @param request       The request to be sent.
     * @param rp            The request processor.
     * @throws Exception Any uncaught exceptions raised while processing the request.
     */
    final public void acceptRequest(RequestSource requestSource,
                                       Object request,
                                       ResponseProcessor rp)
            throws Exception {
        System.err.println(rp);
        rp.process(get());
    }
    
    final public Object get() {
        return internals.data.get(name);
    }
}
