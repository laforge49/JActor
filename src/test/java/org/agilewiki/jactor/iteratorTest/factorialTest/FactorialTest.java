package org.agilewiki.jactor.iteratorTest.factorialTest;

import junit.framework.TestCase;
import org.agilewiki.jactor.JAFuture;
import org.agilewiki.jactor.JAMailboxFactory;
import org.agilewiki.jactor.MailboxFactory;
import org.agilewiki.jactor.SimpleRequest;

/**
 * Test code.
 */
public class FactorialTest extends TestCase {
    public void testFactorial() throws Exception {
        MailboxFactory mailboxFactory = JAMailboxFactory.newMailboxFactory(1);
        try {
            Factorial factorial = new Factorial();
            factorial.initialize(mailboxFactory.createMailbox());
            JAFuture future = new JAFuture();
            SimpleRequest.req.send(future, factorial);
        } finally {
            mailboxFactory.close();
        }
    }
}
