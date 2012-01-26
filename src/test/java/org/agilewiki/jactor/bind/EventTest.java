package org.agilewiki.jactor.bind;

import junit.framework.TestCase;
import org.agilewiki.jactor.*;
import org.agilewiki.jactor.lpc.RequestSource;

public class EventTest extends TestCase {
    public void test() {
        MailboxFactory mailboxFactory = JAMailboxFactory.newMailboxFactory(1);
        try {
            Actor a = new A(mailboxFactory.createMailbox());
            JAEvent event = new JAEvent();
            JAFuture future = new JAFuture();
            Hi hi = new Hi();
            event.sendEvent(a, hi);
            future.send(a, hi);
            Ho ho = new Ho();
            event.sendEvent(a, ho);
            future.send(a, ho);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mailboxFactory.close();
        }
    }

    class Hi {
    }

    class Ho {
    }

    class A extends JBActor {

        A(Mailbox mailbox) {
            super(mailbox);

            bind(Hi.class.getName(), new MethodBinding() {
                public void processRequest(Internals internals, Object request, ResponseProcessor rp)
                        throws Exception {
                    System.err.println("A got request");
                    rp.process("Hello world!");
                }
            });

            bind(Ho.class.getName(), new SyncBinding() {
                @Override
                public void acceptRequest(RequestReceiver requestReceiver,
                                          RequestSource requestSource,
                                          Object request,
                                          ResponseProcessor rp)
                        throws Exception {
                    System.err.println("A got request");
                    rp.process("Hello world!");
                }
            });
        }
    }
}
