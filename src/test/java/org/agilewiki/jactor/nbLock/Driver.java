package org.agilewiki.jactor.nbLock;

import org.agilewiki.jactor.RP;

/**
 * Test code.
 */
public class Driver extends JANBLock {
    /**
     * The application method for processing requests sent to the actor.
     *
     * @param request A request.
     * @param rp      The response processor.
     * @throws Exception Any uncaught exceptions raised while processing the request.
     */
    @Override
    protected void processRequest(Object request, final RP rp)
            throws Exception {

        Class reqcls = request.getClass();

        if (reqcls == DoIt.class) {

            final RP<Object> rpc = new RP<Object>() {
                int count = 3;

                @Override
                public void processResponse(Object response) throws Exception {
                    count -= 1;
                    if (count == 0)
                        rp.processResponse(null);
                }
            };

            Lock.req.send(this, this, new RP<Object>() {
                @Override
                public void processResponse(Object response) throws Exception {
                    System.out.println("start 1");
                    Thread.sleep(100);
                    System.out.println("  end 1");
                    Unlock.req.send(Driver.this, Driver.this, rpc);
                }
            });

            Lock.req.send(this, this, new RP<Object>() {
                @Override
                public void processResponse(Object response) throws Exception {
                    System.out.println("start 2");
                    Thread.sleep(100);
                    System.out.println("  end 2");
                    Unlock.req.send(Driver.this, Driver.this, rpc);
                }
            });

            Lock.req.send(this, this, new RP<Object>() {
                @Override
                public void processResponse(Object response) throws Exception {
                    System.out.println("start 3");
                    Thread.sleep(100);
                    System.out.println("  end 3");
                    Unlock.req.send(Driver.this, Driver.this, rpc);
                }
            });

            return;
        }

        super.processRequest(request, rp);
    }
}
