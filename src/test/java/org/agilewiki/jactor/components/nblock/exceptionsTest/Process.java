package org.agilewiki.jactor.components.nblock.exceptionsTest;

import org.agilewiki.jactor.ExceptionHandler;
import org.agilewiki.jactor.RP;
import org.agilewiki.jactor.bind.Internals;
import org.agilewiki.jactor.bind.MethodBinding;
import org.agilewiki.jactor.components.Component;
import org.agilewiki.jactor.components.Include;
import org.agilewiki.jactor.components.actorName.ActorName;
import org.agilewiki.jactor.components.actorName.GetActorName;
import org.agilewiki.jactor.components.nbLock.Lock;
import org.agilewiki.jactor.components.nbLock.Unlock;

import java.util.ArrayList;

/**
 * Test code.
 */
public class Process extends Component {
    @Override
    public ArrayList<Include> includes() {
        ArrayList<Include> rv = new ArrayList<Include>();
        rv.add(new Include(ActorName.class));
        return rv;
    }

    @Override
    public void bindery() throws Exception {
        thisActor.bind(DoItEx.class.getName(), new MethodBinding<DoItEx, Object>() {
            @Override
            public void processRequest(final Internals internals, DoItEx request, final RP<Object> rp)
                    throws Exception {
                final String me = GetActorName.req.call(internals, thisActor);
                Lock.req.send(internals, thisActor, new RP<Object>() {
                    @Override
                    public void processResponse(Object response) throws Exception {
                        internals.setExceptionHandler(new ExceptionHandler() {
                            @Override
                            public void process(Exception exception) throws Exception {
                                System.out.println(me + " got exception " + exception);
                                Unlock.req.send(internals, thisActor, rp);
                            }
                        });
                        System.out.println("start " + me);
                        Thread.sleep(100);
                        throw new Exception("from " + me);
                    }
                });
            }
        });
    }
}
