package org.agilewiki.jactor.basics;

import org.agilewiki.jactor.Actor;
import org.agilewiki.jactor.lpc.ConcurrentRequest;

/**
 * Test code.
 */
public class GetWidget extends ConcurrentRequest<String, Actor5> {
    public static GetWidget req = new GetWidget();

    @Override
    protected String _call(Actor5 targetActor) throws Exception {
        return targetActor.getWidget();
    }

    @Override
    public boolean isTargetType(Actor targetActor) {
        return targetActor instanceof Actor5;
    }
}
