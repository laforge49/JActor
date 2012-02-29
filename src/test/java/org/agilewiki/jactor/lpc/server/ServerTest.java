package org.agilewiki.jactor.lpc.server;

import junit.framework.TestCase;
import org.agilewiki.jactor.*;
import org.agilewiki.jactor.lpc.JLPCActor;

import java.util.Random;

public class ServerTest extends TestCase {
    public void test() {
        MailboxFactory mailboxFactory = JAMailboxFactory.newMailboxFactory(10);
        try {
            Mailbox mailbox = mailboxFactory.createMailbox();
            JAFuture future = new JAFuture();
            int times = new Random().nextInt(20);
            System.out.println("run " + times + " tasks");
            Driver driver = new Driver(mailbox);
            future.send(driver, times);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mailboxFactory.close();
        }
    }
}

class Driver extends JLPCActor {
    Driver(Mailbox mailbox) {
        super(mailbox);
    }

    @Override
    protected void processRequest(Object request, final RP rp) throws Exception {
        final int times = (Integer) request;
        RP counter = new RP() {
            int rem = times;

            @Override
            public void processResponse(Object response) throws Exception {
                rem -= 1;
                if (rem == 0)
                    rp.processResponse(null);
            }
        };
        for (int i = 0; i < times; i++) {
            Worker worker = new Worker(getMailbox().getMailboxFactory().createAsyncMailbox());
            WorkRequest wr = new WorkRequest();
            wr.id = i;
            send(worker, wr, counter);
        }
    }
}