package org.agilewiki.jactor.continuation;

import junit.framework.TestCase;
import org.agilewiki.jactor.*;
import org.agilewiki.jactor.lpc.JLPCActor;
import org.agilewiki.jactor.lpc.Request;

public class ContinuationTest extends TestCase {
    public void test() throws Exception {
        MailboxFactory mailboxFactory = JAMailboxFactory.newMailboxFactory(1);
        try {
            Driver driver = new Driver();
            driver.initialize(mailboxFactory.createMailbox());
            JAFuture future = new JAFuture();
            System.out.println(">>> " + Doit.req.send(future, driver) + " <<<");
        } finally {
            mailboxFactory.close();
        }
    }
}

class Doit extends Request<String, Driver> {
    public static Doit req = new Doit();

    @Override
    public boolean isTargetType(Actor targetActor) {
        return targetActor instanceof Driver;
    }

    @Override
    public void processRequest(JLPCActor targetActor, RP rp) throws Exception {
        ((Driver) targetActor).doit(rp);
    }
}

class Driver extends JLPCActor {
    void doit(RP<String> rp) {
        Continuation<String> continuation = new Continuation(this, rp);
        Application applicatin = new Application();
        applicatin.continuation = continuation;
        applicatin.start();
    }
}

class Application extends Thread {
    Continuation<String> continuation;

    @Override
    public void run() {
        try {
            continuation.processResponse("Hello world!");
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
    }
}