package org.agilewiki.jactor.bind;

import org.agilewiki.jactor.lpc.Request;

/**
 * A request not handled by a JLPCActor subclass.
 */
public class JBRequest<RESPONSE_TYPE>
        extends Request<RESPONSE_TYPE, NoJLPCActor> {
}
