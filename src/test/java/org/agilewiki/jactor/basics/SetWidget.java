package org.agilewiki.jactor.basics;

import org.agilewiki.jactor.Actor;
import org.agilewiki.jactor.lpc.InitializationRequest;

/**
 * Test code.
 */
public class SetWidget extends InitializationRequest<Object, Actor5> {
    public final String widget;

    public SetWidget(String widget) {
        this.widget = widget;
    }

    @Override
    protected Object _call(Actor5 targetActor) throws Exception {
        targetActor.setWidget(widget);
        return null;
    }

    @Override
    public boolean isTargetType(Actor targetActor) {
        return targetActor instanceof Actor5;
    }
}
