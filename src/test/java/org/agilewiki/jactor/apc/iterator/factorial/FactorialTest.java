package org.agilewiki.jactor.apc.iterator.factorial;

import junit.framework.TestCase;
import org.agilewiki.jactor.lpc.JAFuture;
import org.agilewiki.jactor.lpc.JAMailboxFactory;
import org.agilewiki.jactor.lpc.MailboxFactory;

public class FactorialTest extends TestCase {
    public void testFactorial() throws Exception {
        MailboxFactory mailboxFactory = JAMailboxFactory.newMailboxFactory(1);
        try {
            Factorial factorial = new Factorial(mailboxFactory.createMailbox());
            JAFuture future = new JAFuture();
            future.send(factorial, null);
        } finally {
            mailboxFactory.close();
        }
    }
}
