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

import java.util.ArrayList;

final public class Driver1 extends Component {
    @Override
    public ArrayList<Include> includes() {
        ArrayList<Include> rv = new ArrayList<Include>();
        rv.add(new Include(PubSub.class));
        return rv;
    }

    @Override
    public void bindery() throws Exception {
        super.bindery();
        thisActor.bind(Timing.class.getName(), new MethodBinding<Timing>() {
            @Override
            public void processRequest(final Internals internals, Timing request, ResponseProcessor rp)
                    throws Exception {
                Timing timing = (Timing) request;
                final int count = timing.getCount();
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
                        internals.send(internals.getThisActor(), publish, new ResponseProcessor() {
                            @Override
                            public void process(Object response) throws Exception {
                                rp1.process(null);
                            }
                        });
                    }
                };
                jaIterator.iterate(rp);
            }
        });
    }
}
