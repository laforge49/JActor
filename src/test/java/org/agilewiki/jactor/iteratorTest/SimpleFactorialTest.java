package org.agilewiki.jactor.iteratorTest;

import junit.framework.TestCase;
import org.agilewiki.jactor.JAIterator;
import org.agilewiki.jactor.RP;

/**
 * Test code.
 */
public class SimpleFactorialTest extends TestCase {
    public void testFactorial() throws Exception {
        final int max = 5;
        RP printResult = new RP() {
            public void processResponse(Object rsp) {
                System.out.println(rsp);
            }
        };

        (new JAIterator() {
            int i;
            int r = 1;

            public void process(RP rp) throws Exception {
                if (i >= max) rp.processResponse(new Integer(r));
                else {
                    i += 1;
                    r = r * i;
                    rp.processResponse(null);
                }
            }
        }).iterate(printResult);
    }
}
