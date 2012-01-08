package org.agilewiki.jactor.bind;

import junit.framework.TestCase;
import org.agilewiki.jactor.*;

public class ParentTest extends TestCase {
    public void test() {
        MailboxFactory mailboxFactory = JAMailboxFactory.newMailboxFactory(1);
        try {
            Actor a = new A(mailboxFactory.createMailbox());
            JBActor b = new JBActor(mailboxFactory.createMailbox());
            b.parent = a;
            JAFuture future = new JAFuture();
            System.err.println(future.send(b, new Hi()));
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
