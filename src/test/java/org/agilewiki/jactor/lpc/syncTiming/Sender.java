package org.agilewiki.jactor.lpc.syncTiming;

import org.agilewiki.jactor.RP;
import org.agilewiki.jactor.lpc.JLPCActor;

public class Sender extends JLPCActor {
    public Echo echo;
    public long count;
    boolean async;
    boolean sync;
    int i = 0;

    public void sender(final RP rp) throws Exception {
        while (true) {
            i += 1;
            if (i > count) {
                rp.processResponse(null);
                return;
            }
            async = false;
            sync = false;
            DoEcho.req.send(this, echo, new RP<Object>() {
                @Override
                public void processResponse(Object response) throws Exception {
                    if (!async) {
                        sync = true;
                    } else {
                        sender(rp);
                    }
                }
            });
            if (!sync) {
                async = true;
                return;
            }
        }
    }
}
