package org.agilewiki.jactor.basics;

import org.agilewiki.jactor.Actor;
import org.agilewiki.jactor.lpc.Request;

/**
 * Test code.
 */
public class Hi1 extends Request<String, Actor1> {
    public static final Hi1 req = new Hi1();

    @Override
    public boolean isTargetType(Actor targetActor) {
        return targetActor instanceof Actor1;
    }
}
