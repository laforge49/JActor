package org.agilewiki.jactor.basics;

import org.agilewiki.jactor.Actor;
import org.agilewiki.jactor.lpc.SynchronousRequest;

/**
 * Test code.
 */
public class SetXY extends SynchronousRequest<Object, Actor6> {
    public final int x;
    public final int y;

    public SetXY(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    protected Object _call(Actor6 targetActor) throws Exception {
        targetActor.setXY(x, y);
        return null;
    }

    @Override
    public boolean isTargetType(Actor targetActor) {
        return targetActor instanceof Actor6;
    }
}
