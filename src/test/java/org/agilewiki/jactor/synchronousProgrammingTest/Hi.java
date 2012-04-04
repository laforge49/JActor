package org.agilewiki.jactor.synchronousProgrammingTest;

import org.agilewiki.jactor.Actor;
import org.agilewiki.jactor.lpc.SynchronousRequest;

/**
 * Test code.
 */
public class Hi extends SynchronousRequest<String, Greeter> {
    @Override
    protected String _call(Greeter targetActor) throws Exception {
        return targetActor.hi();
    }

    @Override
    public boolean isTargetType(Actor targetActor) {
        return targetActor instanceof Greeter;
    }
}
