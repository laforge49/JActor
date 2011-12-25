package org.agilewiki.jactor.chain;

import org.agilewiki.jactor.Actor;

public class SendVV implements Send {
    private Actor targetActor;
    private Object request;

    private String resultName;

    public SendVV(Actor targetActor, Object request, String resultName) {
        this.targetActor = targetActor;
        this.request = request;
        this.resultName = resultName;
    }

    @Override
    public Actor getTargetActor() {
        return targetActor;
    }

    @Override
    public Object getRequest() {
        return request;
    }

    @Override
    public String getResultName() {
        return resultName;
    }
}
