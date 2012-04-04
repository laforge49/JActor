package org.agilewiki.jactor.multithreadingTest.exceptions;

import org.agilewiki.jactor.JAFuture;
import org.agilewiki.jactor.JAMailboxFactory;
import org.junit.Test;

/**
 * Test code.
 */
public class ExceptionTest {
    @Test
    public void test() {
        JAMailboxFactory mailboxFactory = JAMailboxFactory.newMailboxFactory(10);
        try {
            Divider a = new Divider(mailboxFactory.createMailbox());
            JAFuture future = new JAFuture();

            try {
                (new SyncDivide(3, 0)).send(future, a);
            } catch (Exception x) {
                System.out.println("test 1 => " + x.toString());
            }

            try {
                (new Divide(3, 0)).send(future, a);
            } catch (Exception x) {
                System.out.println("test 2 => " + x.toString());
            }

            Integer r = (new ISyncDivide(3, 0)).send(future, a);
            System.out.println("test 3 => " + r);
            r = (new IDivide(3, 0)).send(future, a);
            System.out.println("test 4 => " + r);

            try {
                (new Divide(3, 0)).send(future, a);
            } catch (Exception x) {
                System.out.println("test 5 => " + x.toString());
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mailboxFactory.close();
        }
    }
}
