package org.agilewiki.jactor.chain;

import org.agilewiki.jactor.Actor;

public class SendVF implements Send {
    private Actor targetActor;
    private RequestFunc request;

    private String resultName;

    public SendVF(Actor targetActor, RequestFunc request, String resultName) {
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
        return request.get();
    }

    @Override
    public String getResultName() {
        return resultName;
    }
}
