package org.agilewiki.jactor.lpc.serverTest;

import junit.framework.TestCase;
import org.agilewiki.jactor.*;
import org.agilewiki.jactor.lpc.JLPCActor;

import java.util.Random;

/**
 * Test code.
 */
public class ServerTest extends TestCase {
    public void test() {
        MailboxFactory mailboxFactory = JAMailboxFactory.newMailboxFactory(10);
        try {
            Mailbox mailbox = mailboxFactory.createMailbox();
            JAFuture future = new JAFuture();
            int times = new Random().nextInt(20) + 1;
            System.out.println("run " + times + " tasks");
            Driver driver = new Driver();
            driver.initialize(mailbox);
            driver.times = times;
            SimpleRequest.req.send(future, driver);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mailboxFactory.close();
        }
    }
}

/**
 * Test code.
 */
class Driver extends JLPCActor implements SimpleRequestReceiver {
    public int times;

    @Override
    public void processRequest(SimpleRequest request, final RP rp) throws Exception {
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
            Worker worker = new Worker();
            worker.initialize(getMailbox().getMailboxFactory().createAsyncMailbox());
            WorkRequest wr = new WorkRequest();
            wr.id = i;
            send(worker, wr, counter);
        }
    }
}