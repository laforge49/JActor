package org.agilewiki.jactor.basics;

import org.agilewiki.jactor.Actor;
import org.agilewiki.jactor.lpc.Request;

/**
 * Test code.
 */
public class Delay extends Request<Object, Actor7> {
    public static final Delay req = new Delay();

    @Override
    public boolean isTargetType(Actor targetActor) {
        return targetActor instanceof Actor7;
    }
}
