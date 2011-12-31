package org.agilewiki.jactor.iterator;

import junit.framework.TestCase;
import org.agilewiki.jactor.JAIterator;
import org.agilewiki.jactor.ResponseProcessor;

public class SimpleFactorialTest extends TestCase {
    public void testFactorial() throws Exception {
        final int max = 5;
        ResponseProcessor printResult = new ResponseProcessor() {
            public void process(Object rsp) {
                System.out.println(rsp);
            }
        };

        (new JAIterator() {
            int i;
            int r = 1;

            public void process(ResponseProcessor rp) throws Exception {
                if (i >= max) rp.process(new Integer(r));
                else {
                    i += 1;
                    r = r * i;
                    rp.process(null);
                }
            }
        }).iterate(printResult);
    }
}
