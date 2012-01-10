package org.agilewiki.jactor.bind;

import org.agilewiki.jactor.ResponseProcessor;
import org.agilewiki.jactor.lpc.RequestSource;

/**
 * Binds a request class to a concurrent data item.
 * Requests are processed immediately,
 * even if the actor has an asynchronous mailbox.
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
     * <p>
     * The result returned is the concurrent data item named in the constructor, or null.
     * </p>
     *
     * @param requestSource The originator of the request.
     * @param request       The request to be sent.
     * @param rp            The request processor.
     * @throws Exception Any uncaught exceptions raised while processing the request.
     */
    @Override
    final public void acceptRequest(RequestSource requestSource,
                                       Object request,
                                       ResponseProcessor rp)
            throws Exception {
        processRequest(request, rp);
    }


    /**
     * The result returned is the concurrent data item named in the constructor, or null.
     *
     * @param request           A request.
     * @param rp The response processor.
     * @throws Exception Any uncaught exceptions raised while processing the request.
     */
    @Override
    final protected void processRequest(Object request, ResponseProcessor rp)
            throws Exception {
        rp.process(internals.data.get(name));
    }
}
