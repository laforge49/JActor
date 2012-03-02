package org.agilewiki.jactor.components.nblock;

import org.agilewiki.jactor.RP;
import org.agilewiki.jactor.bind.Internals;
import org.agilewiki.jactor.bind.MethodBinding;
import org.agilewiki.jactor.components.Component;
import org.agilewiki.jactor.components.nbLock.Lock;
import org.agilewiki.jactor.components.nbLock.Unlock;

public class Driver extends Component {
    /**
     * Bind request classes.
     *
     * @throws Exception Any exceptions thrown while binding.
     */
    @Override
    public void bindery() throws Exception {
        thisActor.bind(DoIt.class.getName(), new MethodBinding<DoIt, Object>() {
            @Override
            public void processRequest(final Internals internals, DoIt request, final RP<Object> rp) throws Exception {

                final RP<Object> rpc = new RP<Object>() {
                    int count = 3;

                    @Override
                    public void processResponse(Object response) throws Exception {
                        count -= 1;
                        if (count == 0)
                            rp.processResponse(null);
                    }
                };

                Lock.req.send(internals, thisActor, new RP<Object>() {
                    @Override
                    public void processResponse(Object response) throws Exception {
                        System.out.println("start 1");
                        Thread.sleep(100);
                        System.out.println("  end 1");
                        Unlock.req.send(internals, thisActor, rpc);
                    }
                });

                Lock.req.send(internals, thisActor, new RP<Object>() {
                    @Override
                    public void processResponse(Object response) throws Exception {
                        System.out.println("start 2");
                        Thread.sleep(100);
                        System.out.println("  end 2");
                        Unlock.req.send(internals, thisActor, rpc);
                    }
                });

                Lock.req.send(internals, thisActor, new RP<Object>() {
                    @Override
                    public void processResponse(Object response) throws Exception {
                        System.out.println("start 3");
                        Thread.sleep(100);
                        System.out.println("  end 3");
                        Unlock.req.send(internals, thisActor, rpc);
                    }
                });
            }
        });
    }
}
