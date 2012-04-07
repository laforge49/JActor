package org.agilewiki.jactor.basics;

import org.agilewiki.jactor.Mailbox;
import org.agilewiki.jactor.MailboxFactory;
import org.agilewiki.jactor.RP;
import org.agilewiki.jactor.lpc.JLPCActor;

public class Actor3 extends JLPCActor implements Greeter {
    public Actor3(Mailbox mailbox) {
        super(mailbox);
    }

    @Override
    protected void processRequest(Object request, final RP rp) throws Exception {
        Greet1.req.send(this, getParent(), new RP<Object>() {
            @Override
            public void processResponse(Object response) throws Exception {
                MailboxFactory mailboxFactory = getMailbox().getMailboxFactory();
                mailboxFactory.close();
            }
        });
    }
}
