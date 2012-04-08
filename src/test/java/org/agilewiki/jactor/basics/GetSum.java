package org.agilewiki.jactor.basics;

import org.agilewiki.jactor.Actor;
import org.agilewiki.jactor.lpc.SynchronousRequest;

public class GetSum extends SynchronousRequest<Integer, Actor6> {
    public static GetSum req = new GetSum();

    @Override
    protected Integer _call(Actor6 targetActor) throws Exception {
        return targetActor.getSum();
    }

    @Override
    public boolean isTargetType(Actor targetActor) {
        return targetActor instanceof Actor6;
    }
}
