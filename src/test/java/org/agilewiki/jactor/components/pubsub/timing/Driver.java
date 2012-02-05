package org.agilewiki.jactor.components.pubsub.timing;

import org.agilewiki.jactor.JAIterator;
import org.agilewiki.jactor.JANull;
import org.agilewiki.jactor.ResponseProcessor;
import org.agilewiki.jactor.bind.Internals;
import org.agilewiki.jactor.bind.MethodBinding;
import org.agilewiki.jactor.components.Component;
import org.agilewiki.jactor.components.Include;
import org.agilewiki.jactor.components.pubsub.PubSub;
import org.agilewiki.jactor.components.pubsub.Publish;
import org.agilewiki.jactor.parallel.JAResponseCounter;

import java.util.ArrayList;

public class Driver extends Component {
    @Override
    public ArrayList<Include> includes() {
        ArrayList<Include> rv = new ArrayList<Include>();
        rv.add(new Include(PubSub.class));
        return rv;
    }

    @Override
    public void bindery() throws Exception {
        super.bindery();
        thisActor.bind(Timing.class.getName(), new MethodBinding() {
            @Override
            public void processRequest(final Internals internals, Object request, ResponseProcessor rp)
                    throws Exception {
                Timing timing = (Timing) request;
                final int count = timing.getCount();
                final int burst = timing.getBurst();
                JAIterator jaIterator = new JAIterator() {
                    int i = 0;
                    Publish publish = new Publish(new NullRequest());

                    @Override
                    protected void process(final ResponseProcessor rp1) throws Exception {
                        if (i == count) {
                            rp1.process(JANull.jan);
                            return;
                        }
                        i += 1;
                        ResponseProcessor rp2 = new JAResponseCounter(burst, rp1);
                        int j = 0;
                        while (j < burst) {
                            internals.send(internals.getThisActor(), publish, rp2);
                            j += 1;
                        }
                    }
                };
                jaIterator.iterate(rp);
            }
        });
    }
}
