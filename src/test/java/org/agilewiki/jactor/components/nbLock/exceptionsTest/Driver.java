package org.agilewiki.jactor.components.nbLock.exceptionsTest;

import org.agilewiki.jactor.RP;
import org.agilewiki.jactor.bind.Internals;
import org.agilewiki.jactor.bind.MethodBinding;
import org.agilewiki.jactor.bind.Open;
import org.agilewiki.jactor.components.Component;
import org.agilewiki.jactor.components.Include;
import org.agilewiki.jactor.components.JCActor;
import org.agilewiki.jactor.components.actorName.SetActorName;

/**
 * Test code.
 */
public class Driver extends Component {
    /**
     * Bind request classes.
     *
     * @throws Exception Any exceptions thrown while binding.
     */
    @Override
    public void bindery() throws Exception {
        thisActor.bind(DoItEx.class.getName(), new MethodBinding<DoItEx, Object>() {
            @Override
            public void processRequest(final Internals internals, DoItEx request, final RP<Object> rp)
                    throws Exception {

                final RP<Object> rpc = new RP<Object>() {
                    int count = 3;

                    @Override
                    public void processResponse(Object response) throws Exception {
                        count -= 1;
                        if (count == 0)
                            rp.processResponse(null);
                    }
                };

                JCActor p1 = new JCActor(thisActor.getMailbox());
                p1.setParent(thisActor);
                (new Include(Process.class)).call(p1);
                (new SetActorName("1")).call(internals, p1);
                Open.req.call(internals, p1);

                JCActor p2 = new JCActor(thisActor.getMailbox());
                p2.setParent(thisActor);
                (new Include(Process.class)).call(p2);
                (new SetActorName("2")).call(internals, p2);
                Open.req.call(internals, p2);

                JCActor p3 = new JCActor(thisActor.getMailbox());
                p3.setParent(thisActor);
                (new Include(Process.class)).call(p3);
                (new SetActorName("3")).call(internals, p3);
                Open.req.call(internals, p3);

                request.send(internals, p1, rpc);
                request.send(internals, p2, rpc);
                request.send(internals, p3, rpc);
            }
        });
    }
}
