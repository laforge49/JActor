package org.agilewiki.jactor.basics;

import org.agilewiki.jactor.MailboxFactory;
import org.agilewiki.jactor.RP;
import org.agilewiki.jactor.lpc.JLPCActor;

/**
 * Test code.
 */
public class Actor3 extends JLPCActor implements Greeter {
    @Override
    public void processRequest(Greet1 request, final RP rp) throws Exception {
        request.send(this, getParent(), new RP<Object>() {
            @Override
            public void processResponse(Object response) throws Exception {
                MailboxFactory mailboxFactory = getMailbox().getMailboxFactory();
                mailboxFactory.close();
            }
        });
    }
}
