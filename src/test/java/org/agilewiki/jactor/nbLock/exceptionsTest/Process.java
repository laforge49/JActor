package org.agilewiki.jactor.nbLock.exceptionsTest;

import org.agilewiki.jactor.ExceptionHandler;
import org.agilewiki.jactor.RP;
import org.agilewiki.jactor.nbLock.Lock;
import org.agilewiki.jactor.nbLock.Unlock;
import org.agilewiki.jactor.pubsub.actorName.JActorName;

/**
 * Test code.
 */
public class Process extends JActorName implements Does {
    @Override
    public void does(final RP rp) throws Exception {
        final String me = getActorName();
        Lock.req.send(this, this, new RP<Object>() {
            @Override
            public void processResponse(final Object response) throws Exception {
                setExceptionHandler(new ExceptionHandler() {
                    @Override
                    public void process(final Throwable exception)
                            throws Exception {
                        Unlock.req.send(Process.this, Process.this,
                                new RP<Object>() {
                                    @Override
                                    public void processResponse(
                                            final Object response)
                                            throws Exception {
                                        rp.processResponse(null);
                                    }
                                });
                    }
                });
                Thread.sleep(100);
                throw new Exception("from " + me);
            }
        });
    }
}
