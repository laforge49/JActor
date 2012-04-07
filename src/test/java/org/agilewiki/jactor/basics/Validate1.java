package org.agilewiki.jactor.basics;

import org.agilewiki.jactor.Actor;
import org.agilewiki.jactor.lpc.Request;

public class Validate1 extends Request<Boolean, Actor4> {
    public static final Validate1 req = new Validate1();

    @Override
    public boolean isTargetType(Actor targetActor) {
        return targetActor instanceof Actor4;
    }
}
