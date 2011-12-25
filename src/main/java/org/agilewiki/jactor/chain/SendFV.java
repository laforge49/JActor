package org.agilewiki.jactor.chain;

import org.agilewiki.jactor.Actor;

public class SendFV implements Send {
    private ActorFunc targetActor;
    private Object request;

    private String resultName;

    public SendFV(ActorFunc targetActor, Object request, String resultName) {
        this.targetActor = targetActor;
        this.request = request;
        this.resultName = resultName;
    }

    @Override
    public Actor getTargetActor() {
        return targetActor.get();
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
