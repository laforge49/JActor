package org.agilewiki.jactor.components.pubsub.timingTest;

import org.agilewiki.jactor.JAIterator;
import org.agilewiki.jactor.JANull;
import org.agilewiki.jactor.RP;
import org.agilewiki.jactor.bind.Internals;
import org.agilewiki.jactor.bind.MethodBinding;
import org.agilewiki.jactor.components.Component;
import org.agilewiki.jactor.components.Include;
import org.agilewiki.jactor.components.pubsub.PubSub;
import org.agilewiki.jactor.components.pubsub.Publish;
import org.agilewiki.jactor.parallel.JAResponseCounter;

import java.util.ArrayList;

/**
 * Test code.
 */
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
        thisActor.bind(Timing.class.getName(), new MethodBinding<Timing, Object>() {
            @Override
            public void processRequest(final Internals internals, Timing request, RP rp)
                    throws Exception {
                Timing timing = (Timing) request;
                final int count = timing.getCount();
                final int burst = timing.getBurst();
                JAIterator jaIterator = new JAIterator() {
                    int i = 0;
                    Publish publish = new Publish(new NullRequest());

                    @Override
                    protected void process(final RP rp1) throws Exception {
                        if (i == count) {
                            rp1.processResponse(JANull.jan);
                            return;
                        }
                        i += 1;
                        RP rp2 = new JAResponseCounter(burst, rp1);
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
