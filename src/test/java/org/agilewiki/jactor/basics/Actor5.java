package org.agilewiki.jactor.basics;

import org.agilewiki.jactor.Mailbox;
import org.agilewiki.jactor.RP;
import org.agilewiki.jactor.lpc.JLPCActor;

/**
 * Test code.
 */
public class Actor5 extends JLPCActor {
    private String widget;

    public Actor5(Mailbox mailbox) {
        super(mailbox);
    }

    String getWidget() {
        return widget;
    }

    void setWidget(String widget) {
        this.widget = widget;
    }

    @Override
    protected void processRequest(Object request, final RP rp) throws Exception {
        Class reqcls = request.getClass();

        if (reqcls == GetWidget.class) {
            rp.processResponse(getWidget());
            return;
        }

        throw new UnsupportedOperationException(reqcls.getName());
    }
}
