package org.agilewiki.jactor.chain;

import org.agilewiki.jactor.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Composes a series of calls to send.
 */
abstract public class Chain {
    private Actor sourceActor;
    private ArrayList<Send> sends = new ArrayList<Send>();
    private HashMap<String, Object> results = new HashMap<String, Object>();
    
    public Chain(Actor sourceActor) {
        this.sourceActor = sourceActor;
    }
    
    final public Object get(String resultName) {
        return results.get(resultName);
    }

    final public void add(Actor targetActor, Object request) {
        sends.add(new SendVV(targetActor, request, null));
    }

    final public void add(Actor targetActor, Object request, String resultName) {
        sends.add(new SendVV(targetActor, request, resultName));
    }

    final public void add(ActorFunc targetActor, RequestFunc request) {
        sends.add(new SendFF(targetActor, request, null));
    }

    final public void add(ActorFunc targetActor, RequestFunc request, String resultName) {
        sends.add(new SendFF(targetActor, request, resultName));
    }

    final public void add(Actor targetActor, RequestFunc request) {
        sends.add(new SendVF(targetActor, request, null));
    }

    final public void add(Actor targetActor, RequestFunc request, String resultName) {
        sends.add(new SendVF(targetActor, request, resultName));
    }

    final public void add(ActorFunc targetActor, Object request) {
        sends.add(new SendFV(targetActor, request, null));
    }

    final public void add(ActorFunc targetActor, Object request, String resultName) {
        sends.add(new SendFV(targetActor, request, resultName));
    }

    final public void eval(ResponseProcessor rp) throws Exception {
        (new JAIterator(rp) {
            Iterator<Send> it = sends.iterator();

            @Override
            protected void process(final ResponseProcessor rp1) throws Exception {
                if (!it.hasNext()) rp1.process(this);
                else {
                    final Send s = it.next();
                    Actor a = s.getTargetActor();
                    Object r = s.getRequest();
                    send(a, r, new ResponseProcessor() {
                        @Override
                        public void process(Object response) throws Exception {
                            String rn = s.getResultName();
                            if (rn != null) results.put(rn, response);
                            rp1.process(null);
                        }
                    });
                }
            }
        }).iterate();
    }
    
    /**
     * Send a request to an actor.
     *
     * @param actor The target actor.
     * @param request The request.
     * @throws Exception Any uncaught exceptions raised while processing the request.
     */
    abstract public void send(Actor actor, Object request, ResponseProcessor rp) throws Exception;
}
