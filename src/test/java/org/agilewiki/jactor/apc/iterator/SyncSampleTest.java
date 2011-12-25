package org.agilewiki.jactor.apc.iterator;

import junit.framework.TestCase;
import org.agilewiki.jactor.apc.JAIterator;
import org.agilewiki.jactor.apc.ResponseProcessor;

public class SyncSampleTest extends TestCase {
    public void testSample() throws Exception {
        final int max = 5;
        ResponseProcessor printResult = new ResponseProcessor() {
            public void process (Object rsp){
                System.out.println(rsp);
            }
        } ;

        new JAIterator(printResult) {
            int i;
            int r;

            public void process(ResponseProcessor rp) throws Exception {
                if (r == 0) r = 1;
                if (i >= max) rp.process(new Integer(r));
                else {
                    i += 1;
                    r = r * i;
                    rp.process(null);
                }
            }
        };
    }
}
