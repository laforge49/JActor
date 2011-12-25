package org.agilewiki.jactor.chain;

import org.agilewiki.jactor.Actor;

public class SendFF implements Send {
    private ActorFunc targetActor;
    private RequestFunc request;

    private String resultName;

    public SendFF(ActorFunc targetActor, RequestFunc request, String resultName) {
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
        return request.get();
    }

    @Override
    public String getResultName() {
        return resultName;
    }
}
