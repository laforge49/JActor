package org.agilewiki.jactor.advanced.allInOne;

import org.agilewiki.jactor.Actor;
import org.agilewiki.jactor.lpc.Request;

abstract public class AllInOneReq extends Request<Object, AllInOne> {
    @Override
    public boolean isTargetType(Actor targetActor) {
        return targetActor instanceof AllInOne;
    }
}
