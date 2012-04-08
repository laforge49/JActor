package org.agilewiki.jactor.basics;

import org.agilewiki.jactor.Actor;
import org.agilewiki.jactor.lpc.Request;

public class Add extends Request<Integer, Actor6a> {
    public final int x;
    public final int y;

    public Add(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean isTargetType(Actor targetActor) {
        return targetActor instanceof Actor6a;
    }
}
