package org.agilewiki.jactor.bind;

import junit.framework.TestCase;
import org.agilewiki.jactor.*;

public class BindTest extends TestCase {
    public void test() {
        MailboxFactory mailboxFactory = JAMailboxFactory.newMailboxFactory(1);
        try {
            Actor a = new A(mailboxFactory.createMailbox());
            JAFuture future = new JAFuture();
            System.err.println(future.send(a, new Hi()));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mailboxFactory.close();
        }
    }

    class Hi {
    }

    class A extends JBActor {

        A(Mailbox mailbox) {
            super(mailbox);

            bind(Hi.class.getName(), new MethodBinding() {
                protected void processRequest(Object request, ResponseProcessor rp)
                        throws Exception {
                    rp.process("Hello world!");
                }
            });
        }
    }
}
