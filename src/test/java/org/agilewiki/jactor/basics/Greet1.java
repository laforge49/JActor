package org.agilewiki.jactor.basics;

import org.agilewiki.jactor.Actor;
import org.agilewiki.jactor.lpc.Request;

/**
 * Test code.
 */
public class Greet1 extends Request<Object, Greeter> {
    public static final Greet1 req = new Greet1();

    @Override
    public boolean isTargetType(Actor targetActor) {
        return targetActor instanceof Greeter;
    }
}
