package org.agilewiki.jactor.components.pubsubComponent.timing;

import org.agilewiki.jactor.JAIterator;
import org.agilewiki.jactor.JANull;
import org.agilewiki.jactor.ResponseProcessor;
import org.agilewiki.jactor.bind.JBActor;
import org.agilewiki.jactor.bind.MethodBinding;
import org.agilewiki.jactor.components.Component;
import org.agilewiki.jactor.components.Include;
import org.agilewiki.jactor.components.pubsubComponent.PubSubComponent;
import org.agilewiki.jactor.parallel.JAResponseCounter;
import org.agilewiki.jactor.pubsub.Publish;

import java.util.ArrayList;

public class Driver extends Component {
    @Override
    public ArrayList<Include> includes() {
        ArrayList<Include> rv = new ArrayList<Include>();
        rv.add(new Include(PubSubComponent.class));
        return rv;
    }

    @Override
    public void open(final JBActor.Internals internals, final ResponseProcessor rp) throws Exception {
        super.open(internals, new ResponseProcessor() {
            @Override
            public void process(Object response) throws Exception {
                internals.bind(Timing.class.getName(), new MethodBinding() {
                    @Override
                    public void processRequest(final JBActor.Internals internals, Object request, ResponseProcessor rp)
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

                rp.process(null);
            }
        });
    }
}
