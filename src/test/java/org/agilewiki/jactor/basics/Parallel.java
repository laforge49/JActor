package org.agilewiki.jactor.basics;

import org.agilewiki.jactor.Actor;
import org.agilewiki.jactor.lpc.Request;

/**
 * Test code.
 */
public class Parallel extends Request<Boolean, Actor5a> {
    public static final Parallel req = new Parallel();

    @Override
    public boolean isTargetType(Actor targetActor) {
        return targetActor instanceof Actor5a;
    }
}
